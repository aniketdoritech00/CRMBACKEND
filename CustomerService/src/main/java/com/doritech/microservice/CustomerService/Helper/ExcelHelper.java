package com.doritech.microservice.CustomerService.Helper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.microservice.CustomerService.Entity.Contact;
import com.doritech.microservice.CustomerService.Entity.Customer;
import com.doritech.microservice.CustomerService.Entity.CustomerAddress;
import com.doritech.microservice.CustomerService.Entity.CustomerAddress.AddressType;
import com.doritech.microservice.CustomerService.Enum.CommunicationMode;
import com.doritech.microservice.CustomerService.Enum.ContactType;
import com.doritech.microservice.CustomerService.Enum.CustomerStatus;
import com.doritech.microservice.CustomerService.ServiceImpl.CustomerGeneratorService;

public class ExcelHelper {

	public static List<Customer> parseExcel(MultipartFile file, CustomerGeneratorService generatorService)
			throws IOException, ParseException {
		List<Customer> customers = new ArrayList<>();
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);

		Row headerRow = null;
		for (int i = 0; i < 10; i++) {
			Row row = sheet.getRow(i);
			if (row == null)
				continue;
			for (Cell cell : row) {
				String cellValue = getCellValue(cell);
				if (cellValue != null && cellValue.trim().equalsIgnoreCase("Company Name")) {
					headerRow = row;
					System.out.println("Found header row at: " + i);
					break;
				}
			}
			if (headerRow != null)
				break;
		}

		if (headerRow == null) {
			workbook.close();
			throw new RuntimeException("No valid header row found (expected 'Company Name')");
		}

		Map<Integer, String> headers = new HashMap<>();
		for (Cell cell : headerRow) {
			String cellValue = getCellValue(cell);
			if (cellValue != null && !cellValue.trim().isEmpty()) {
				headers.put(cell.getColumnIndex(), cellValue.trim());
			}
		}

		SimpleDateFormat[] dateFormats = { new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("dd-MM-yyyy"),
				new SimpleDateFormat("MM/dd/yyyy"), new SimpleDateFormat("dd/MM/yyyy"),
				new SimpleDateFormat("yyyy/MM/dd") };

		for (int i = headerRow.getRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row == null)
				continue;

			boolean hasData = false;
			for (Cell cell : row) {
				String cellValue = getCellValue(cell);
				if (cellValue != null && !cellValue.trim().isEmpty()) {
					hasData = true;
					break;
				}
			}

			if (!hasData) {
				continue;
			}

			Customer customer = new Customer();
			Contact contact = new Contact();
			CustomerAddress address = new CustomerAddress();

			for (Cell cell : row) {
				String header = headers.get(cell.getColumnIndex());
				if (header == null)
					continue;

				String cellValue = getCellValue(cell);

				switch (header.trim()) {
				case "Company Name":
					customer.setName(cellValue);
					break;
				case "Contact Person Name":
					customer.setLegalName(cellValue);
					contact.setFullName(cellValue);

					if (!cellValue.trim().isEmpty()) {
						String[] parts = cellValue.trim().split("\\s+", 2);
						contact.setFirstName(parts.length > 0 ? parts[0] : "");
						contact.setLastName(parts.length > 1 ? parts[1] : "");
					} else {
						contact.setFirstName("");
						contact.setLastName("");
					}
					break;
				case "Designation":
					contact.setDesignation(cellValue);
					break;
				case "Department":
					contact.setDepartment(cellValue);
					break;
				case "Email ID":
					contact.setEmail(cellValue);
					break;
				case "Mobile Number":
					contact.setMobileNumber(cellValue);
					break;
				case "Alternate Number":
					contact.setAlternateNumber(cellValue);
					break;
				case "Landline / Boardline":
					contact.setLandline(cellValue);
					break;
				case "Branch / Regional Office":
					address.setAddressLine1(cellValue);
					address.setType(AddressType.REGISTERED);
					break;
				case "Address (Head Office)":
					address.setAddressLine2(cellValue);
					break;
				case "City":
					address.setCity(cellValue);
					break;
				case "State":
					address.setState(cellValue);
					break;
				case "Pincode":
					address.setPostalCode(cellValue);
					break;
				case "GST / Tax ID":
					customer.setGstNumber(cellValue);
					break;
				case "Industry / Segment":
					customer.setIndustry(cellValue);
					break;
				case "Current Products / Services Used":
					customer.setServiceUsed(cellValue);
					break;
				case "Annual Budget (Approx.)":
					customer.setAnnualBudget(cellValue);
					break;
				case "Contract Type":
					if (!cellValue.trim().isEmpty()) {
						try {
							customer.setContactType(ContactType.fromValue(cellValue));
						} catch (Exception e) {
							customer.setContactType(null);
						}
					}
					break;
				case "Contract Start Date":
					if (!cellValue.trim().isEmpty()) {
						if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
							customer.setContractStartDate(cell.getDateCellValue());
						} else {
							Date parsedDate = parseDate(cellValue, dateFormats);
							customer.setContractStartDate(parsedDate);
						}
					}
					break;
				case "Contract End Date":
					if (!cellValue.trim().isEmpty()) {
						if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
							customer.setContractEndDate(cell.getDateCellValue());
						} else {
							Date parsedDate = parseDate(cellValue, dateFormats);
							customer.setContractEndDate(parsedDate);
						}
					}
					break;
				case "Preferred Communication Mode":
					if (!cellValue.trim().isEmpty()) {
						try {
							customer.setPreferredCommunicationMode(
									CommunicationMode.valueOf(cellValue.trim().toUpperCase()));
						} catch (Exception e) {
						}
					}
					break;
				case "Website":
					customer.setWebsite(cellValue);
					break;
				case "Remarks / Notes":
					customer.setRemakrs(cellValue);
					break;
				case "Gender":
					contact.setGender(cellValue);
					break;
				}
			}

			if (customer.getName() != null && !customer.getName().trim().isEmpty()) {
				customer.setStatus(CustomerStatus.ACTIVE);

				try {
					String customerCode = generatorService.getCustomerValue(1); // Replace 1 with appropriate ID
					customer.setCustomerCode(customerCode); // Adjust field name as per your entity

					contact.setIsPrimary(true);
					contact.setCustomer(customer);
					address.setCustomer(customer);

					customer.getContacts().add(contact);
					customer.getAddresses().add(address);

					customers.add(customer);

					System.out.println("Processing row " + (i + 1) + " - Customer: " + customer.getName() + " - Code: "
							+ customerCode);
				} catch (Exception e) {
					System.err.println("Failed to generate customer code for row " + (i + 1) + " - Customer: "
							+ customer.getName() + " - Error: " + e.getMessage());
					continue;
				}
			} else {
				System.out.println("Skipping row " + (i + 1) + " - No Company Name");
			}
		}

		workbook.close();
		System.out.println("Total customers processed: " + customers.size());
		return customers;
	}

	private static Date parseDate(String dateStr, SimpleDateFormat[] formats) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return null;
		}

		for (SimpleDateFormat format : formats) {
			try {
				return format.parse(dateStr.trim());
			} catch (ParseException e) {
			}
		}
		return null;
	}

	private static String getCellValue(Cell cell) {
		if (cell == null)
			return "";

		try {
			switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue().trim();
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
				} else {
					double val = cell.getNumericCellValue();
					if (val == Math.floor(val)) {
						return String.valueOf((long) val);
					}
					return String.valueOf(val);
				}
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case FORMULA:
				try {
					return cell.getStringCellValue().trim();
				} catch (Exception e) {
					try {
						double val = cell.getNumericCellValue();
						if (val == Math.floor(val)) {
							return String.valueOf((long) val);
						}
						return String.valueOf(val);
					} catch (Exception ex) {
						return "";
					}
				}
			case BLANK:
			default:
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}
}