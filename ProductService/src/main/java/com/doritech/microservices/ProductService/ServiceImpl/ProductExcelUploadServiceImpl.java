package com.doritech.microservices.ProductService.ServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.microservices.ProductService.Entity.Product;
import com.doritech.microservices.ProductService.Repository.ProductRepository;
import com.doritech.microservices.ProductService.Response.ApiResponse;
import com.doritech.microservices.ProductService.Service.ProductExcelUploadService;

@Service
public class ProductExcelUploadServiceImpl implements ProductExcelUploadService {

    private static final Logger log = LoggerFactory.getLogger(ProductExcelUploadServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ApiResponse<Object> uploadProductsFromExcel(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiResponse.error("Uploaded Excel file is empty or missing");
        }

        List<Product> productsToSave = new ArrayList<>();
        int newProducts = 0;
        int updatedProducts = 0;
        int skippedRows = 0;

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null || sheet.getPhysicalNumberOfRows() <= 1) {
                return ApiResponse.error("Excel sheet is empty or missing data");
            }

            Row headerRow = sheet.getRow(0);
            Map<String, Integer> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                if (cell == null) continue;
                String headerName = cell.getStringCellValue().trim().toLowerCase();
                headerMap.put(headerName, cell.getColumnIndex());
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    Product product = parseRowToProduct(row, headerMap);
                    validateProduct(product, i + 1);

                    Optional<Product> existing = productRepository.findByProductCode(product.getProductCode());
                    if (existing.isPresent()) {
                        Product existingProduct = existing.get();
                        existingProduct.setName(product.getName());
                        existingProduct.setDescription(product.getDescription());
                        existingProduct.setCategory(product.getCategory());
                        existingProduct.setPrice(product.getPrice());
                        existingProduct.setCurrency(product.getCurrency());
                        productsToSave.add(existingProduct);
                        updatedProducts++;
                    } else {
                        productsToSave.add(product);
                        newProducts++;
                    }

                } catch (IllegalArgumentException ex) {
                    skippedRows++;
                    log.warn("Row {} skipped: {}", i + 1, ex.getMessage());
                } catch (Exception ex) {
                    skippedRows++;
                    log.error("Unexpected error at row {}: {}", i + 1, ex.getMessage(), ex);
                }
            }

            if (productsToSave.isEmpty()) {
                return ApiResponse.error("No valid product records found in Excel file");
            }

            productRepository.saveAll(productsToSave);

            Map<String, Object> result = new HashMap<>();
            result.put("totalProcessed", productsToSave.size());
            result.put("newProducts", newProducts);
            result.put("updatedProducts", updatedProducts);
            result.put("skippedRows", skippedRows);
            result.put("fileName", file.getOriginalFilename());

            String msg = String.format("Products uploaded successfully. New: %d, Updated: %d, Skipped: %d", newProducts, updatedProducts, skippedRows);

            return ApiResponse.success(msg, result);

        } catch (IOException e) {
            log.error("Error reading Excel file: {}", e.getMessage(), e);
            return ApiResponse.error("Error reading Excel file: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ApiResponse.error("Unexpected server error: " + e.getMessage());
        }
    }

    private Product parseRowToProduct(Row row, Map<String, Integer> headerMap) {
        Product product = new Product();

        product.setProductCode(getCellStringValue(row.getCell(headerMap.get("product code"))));
        product.setName(getCellStringValue(row.getCell(headerMap.get("name"))));
        product.setDescription(getCellStringValue(row.getCell(headerMap.get("description"))));
        product.setCategory(getCellStringValue(row.getCell(headerMap.get("category"))));

        Integer priceIndex = headerMap.get("price");
        if (priceIndex != null) {
            product.setPrice(getCellBigDecimalValue(row.getCell(priceIndex)));
        }

        Integer currencyIndex = headerMap.get("currency");
        if (currencyIndex != null) {
            String currency = getCellStringValue(row.getCell(currencyIndex));
            product.setCurrency(currency.isEmpty() ? "INR" : currency);
        }

        return product;
    }

    private void validateProduct(Product product, int rowNum) {
        if (product.getProductCode() == null || product.getProductCode().isEmpty()) {
            throw new IllegalArgumentException("Missing product code at row " + rowNum);
        }
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Missing product name at row " + rowNum);
        }
        if (product.getPrice() == null) {
            throw new IllegalArgumentException("Missing product price at row " + rowNum);
        }
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private BigDecimal getCellBigDecimalValue(Cell cell) {
        if (cell == null) return BigDecimal.ZERO;
        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                try {
                    return new BigDecimal(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return BigDecimal.ZERO;
                }
            default:
                return BigDecimal.ZERO;
        }
    }
}
