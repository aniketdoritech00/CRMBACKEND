package com.doritech.microservices.ProductService.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.doritech.microservices.ProductService.Entity.CustomerProduct;
import com.doritech.microservices.ProductService.Entity.Product;
import com.doritech.microservices.ProductService.Exception.GlobalException;
import com.doritech.microservices.ProductService.Mapper.CustomerProductMapper;
import com.doritech.microservices.ProductService.Mapper.ProductMapper;
import com.doritech.microservices.ProductService.Repository.CustomerProductRepository;
import com.doritech.microservices.ProductService.Repository.ProductRepository;
import com.doritech.microservices.ProductService.Request.CustomerProductRequest;
import com.doritech.microservices.ProductService.Request.ProductRequest;
import com.doritech.microservices.ProductService.Request.ProductUpdateRequest;
import com.doritech.microservices.ProductService.Response.ApiResponse;
import com.doritech.microservices.ProductService.Response.CustomerProductResponse;
import com.doritech.microservices.ProductService.Response.ProductResponse;
import com.doritech.microservices.ProductService.Service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
//test
	private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Value("${customer.api.url}")
	private String customerApiUrl;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	CustomerProductRepository customerProductRepository;
	@Autowired
	CustomerProductMapper customerProductMapper;
	@Autowired
	ProductMapper productMapper;

	@Override
	public ProductResponse createProduct(ProductRequest request) {
		log.info("Creating product with code: {}", request.getProductCode());

		if (productRepository.existsByProductCode(request.getProductCode())) {
			throw new GlobalException("Product with code " + request.getProductCode() + " already exists");

		}

		Product product = productMapper.toEntity(request);
		Product savedProduct = productRepository.save(product);

		log.info("Product created successfully with ID: {}", savedProduct.getId());
		return productMapper.toResponse(savedProduct);
	}

	@Override
	@Transactional(readOnly = true)
	public ProductResponse getProductById(Long id) {
		log.info("Fetching product with ID: {}", id);

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new GlobalException("Product not found with ID: " + id));

		return productMapper.toResponse(product);
	}

	@Override
	@Transactional(readOnly = true)
	public ProductResponse getProductByCode(String productCode) {
		log.info("Fetching product with code: {}", productCode);

		Product product = productRepository.findByProductCode(productCode)
				.orElseThrow(() -> new GlobalException("Product not found with code: " + productCode));

		return productMapper.toResponse(product);
	}

	@Override
	public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
		log.info("Updating product with ID: {}", id);

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new GlobalException("Product not found with ID: " + id));

		if (request.getName() != null) {
			product.setName(request.getName());
		}
		if (request.getDescription() != null) {
			product.setDescription(request.getDescription());
		}
		if (request.getCategory() != null) {
			product.setCategory(request.getCategory());
		}
		if (request.getPrice() != null) {
			product.setPrice(request.getPrice());
		}
		if (request.getCurrency() != null) {
			product.setCurrency(request.getCurrency());
		}

		Product updatedProduct = productRepository.save(product);

		log.info("Product updated successfully with ID: {}", updatedProduct.getId());
		return productMapper.toResponse(updatedProduct);
	}

	@Override
	public void deleteProduct(Long id) {
		log.info("Deleting product with ID: {}", id);

		if (!productRepository.existsById(id)) {
			throw new GlobalException("Product not found with ID: " + id);
		}

		Long activeAssignments = customerProductRepository.countActiveAssignmentsByProductId(id);
		if (activeAssignments > 0) {
			throw new RuntimeException(
					"Cannot delete product. It is assigned to " + activeAssignments + " active customers");
		}

		productRepository.deleteById(id);
		log.info("Product deleted successfully with ID: {}", id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductResponse> getAllProducts(Pageable pageable) {
		log.info("Fetching all products with pagination");

		Page<Product> products = productRepository.findAll(pageable);
		return products.map(productMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductResponse> searchProducts(String name, String category, Pageable pageable) {
		log.info("Searching products with filters - name: {}, category: {}", name, category);

		Page<Product> products = productRepository.findByFilters(name, category, pageable);
		return products.map(productMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductResponse> searchProducts(String search, Pageable pageable) {
		log.info("Searching products with keyword: {}", search);
		Page<Product> products = productRepository.searchAll(search, pageable);
		return products.map(productMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getAllCategories() {
		log.info("Fetching all product categories");
		return productRepository.findAllCategories();
	}

	@Override
	public CustomerProductResponse assignProductToCustomer(CustomerProductRequest request) {
		log.info("Assigning product {} to customer {}", request.getProductId(), request.getCustomerId());

		if (!productRepository.existsById(request.getProductId())) {
			throw new GlobalException("Product not found with ID: " + request.getProductId());
		}

		if (customerProductRepository.existsByCustomerIdAndProductId(request.getCustomerId(), request.getProductId())) {
			throw new GlobalException("Product is already assigned to this customer");
		}

		CustomerProduct customerProduct = customerProductMapper.toEntity(request);
		CustomerProduct savedCustomerProduct = customerProductRepository.save(customerProduct);

		CustomerProduct customerProductWithProduct = customerProductRepository.findById(savedCustomerProduct.getId())
				.orElseThrow(() -> new GlobalException("Failed to fetch saved customer product"));

		log.info("Product assigned successfully");
		return customerProductMapper.toResponse(customerProductWithProduct);
	}

	@Override
	public void unassignProductFromCustomer(Long customerId, Long productId) {
		log.info("Unassigning product {} from customer {}", productId, customerId);

		CustomerProduct customerProduct = customerProductRepository.findByCustomerIdAndProductId(customerId, productId)
				.orElseThrow(() -> new GlobalException("Product assignment not found"));

		customerProductRepository.delete(customerProduct);
		log.info("Product unassigned successfully");
	}

	@Override
	@Transactional(readOnly = true)
	public List<CustomerProductResponse> getCustomerProducts(Long customerId) {
		log.info("Fetching products for customer: {}", customerId);

		List<CustomerProduct> customerProducts = customerProductRepository.findByCustomerIdWithProduct(customerId);
		return customerProducts.stream().map(customerProductMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<CustomerProductResponse> getProductCustomers(Long productId) {
		log.info("Fetching customers for product: {}", productId);

		List<CustomerProduct> customerProducts = customerProductRepository.findByProductId(productId);
		return customerProducts.stream().map(customerProductMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Long getActiveAssignmentCount(Long productId) {
		log.info("Getting active assignment count for product: {}", productId);
		return customerProductRepository.countActiveAssignmentsByProductId(productId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CustomerProductResponse> getAllCustomerProducts() {
		log.info("Fetching all customer products without pagination");

		List<CustomerProduct> customerProducts = customerProductRepository.findAllWithProduct();
		return customerProducts.stream().map(customerProductMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CustomerProductResponse> getAllCustomerProducts(Pageable pageable) {
		log.info("Fetching all customer products with pagination - page: {}, size: {}", pageable.getPageNumber(),
				pageable.getPageSize());

		Page<CustomerProduct> customerProducts = customerProductRepository.findAllWithProduct(pageable);
		return customerProducts.map(customerProductMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CustomerProductResponse> getAllCustomerProducts(String status, Long customerId, Long productId) {
		log.info("Fetching customer products with filters - status: {}, customerId: {}, productId: {}", status,
				customerId, productId);

		CustomerProduct.ProductStatus statusEnum = null;
		if (status != null && !status.trim().isEmpty()) {
			try {
				statusEnum = CustomerProduct.ProductStatus.valueOf(status.toUpperCase());
			} catch (IllegalArgumentException e) {
				log.warn("Invalid status provided: {}, ignoring filter", status);
			}
		}

		List<CustomerProduct> customerProducts = customerProductRepository.findAllWithFilters(statusEnum, customerId,
				productId);

		return customerProducts.stream().map(customerProductMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CustomerProductResponse> getAllCustomerProducts(String status, Long customerId, Long productId,
			Pageable pageable) {
		log.info(
				"Fetching customer products with filters and pagination - status: {}, customerId: {}, productId: {}, page: {}, size: {}",
				status, customerId, productId, pageable.getPageNumber(), pageable.getPageSize());

		CustomerProduct.ProductStatus statusEnum = null;
		if (status != null && !status.trim().isEmpty()) {
			try {
				statusEnum = CustomerProduct.ProductStatus.valueOf(status.toUpperCase());
			} catch (IllegalArgumentException e) {
				log.warn("Invalid status provided: {}, ignoring filter", status);
			}
		}

		Page<CustomerProduct> customerProducts = customerProductRepository.findAllWithFilters(statusEnum, customerId,
				productId, pageable);

		return customerProducts.map(customerProductMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductResponse> getAllProductsNoPagination() {
		log.info("Fetching all products without pagination");

		List<Product> products = productRepository.findAll();
		return products.stream().map(productMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<CustomerProductResponse> getAllCustomerProductsWithOutPaggination() {
		try {
			List<CustomerProduct> customerProducts = customerProductRepository.findAllWithProduct();
			return customerProducts.stream().map(customerProductMapper::toResponse).collect(Collectors.toList());
		} catch (Exception e) {
			log.error("Error fetching all customer products: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to fetch customer products");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, Object> getCustomerWiseAssignmentsWithOutPaggination() {
		try {
			List<Long> customerIds = customerProductRepository.findDistinctCustomerIds();
			Map<Long, Object> result = new HashMap<>();

			for (Long customerId : customerIds) {
				List<CustomerProduct> products = customerProductRepository.findByCustomerIdWithProduct(customerId);

				int productCount = products.size();
				double totalPrice = products.stream().map(p -> p.getProduct().getPrice().doubleValue()).reduce(0.0,
						Double::sum);

				Map<String, Object> customerData = new HashMap<>();
				customerData.put("products",
						products.stream().map(customerProductMapper::toResponse).collect(Collectors.toList()));
				customerData.put("productCount", productCount);
				customerData.put("totalPrice", totalPrice);

				result.put(customerId, customerData);
			}

			return result;
		} catch (Exception e) {
			log.error("Error fetching customer-wise assignments: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to fetch customer-wise assignments");
		}
	}

	@Override
	public List<ProductResponse> getAllProductsWithOutPaggination() {
		try {
			List<Product> products = productRepository.findAll();
			return products.stream().map(productMapper::toResponse).collect(Collectors.toList());
		} catch (Exception e) {
			log.error("Error fetching all products: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to fetch products");
		}
	}

	@Override
	public Map<String, Object> getAllCustomerProductsWithPaggination(int page, int size) {
		Map<String, Object> response = new HashMap<>();
		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
			Page<CustomerProduct> productPage = customerProductRepository.findAllWithProduct(pageable);

			List<CustomerProductResponse> content = productPage.stream().map(customerProductMapper::toResponse)
					.collect(Collectors.toList());

			response.put("data", content);
			response.put("currentPage", productPage.getNumber());
			response.put("totalItems", productPage.getTotalElements());
			response.put("totalPages", productPage.getTotalPages());
		} catch (Exception e) {
			log.error("Error fetching customer products with pagination: {}", e.getMessage(), e);
			response.put("error", "Failed to fetch customer products");
		}
		return response;
	}


	@Override
	@Transactional(readOnly = true)
	public ApiResponse<Map<String, Object>> getCustomerWiseAssignmentsWithPaggination(int page, int size) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        List<Long> customerIds = customerProductRepository.findDistinctCustomerIds();
	        if (customerIds.isEmpty()) {
	            response.put("data", Collections.emptyMap());
	            response.put("currentPage", 0);
	            response.put("totalItems", 0);
	            response.put("totalPages", 0);
	            return ApiResponse.success("No customer assignments found", response);
	        }

	        int start = page * size;
	        int end = Math.min(start + size, customerIds.size());
	        if (start > end) {
	            response.put("data", Collections.emptyMap());
	            response.put("currentPage", page);
	            response.put("totalItems", customerIds.size());
	            response.put("totalPages", (customerIds.size() + size - 1) / size);
	            return ApiResponse.success("No data available for this page", response);
	        }

	        List<Long> pagedCustomerIds = customerIds.subList(start, end);
	        Map<Long, Object> result = new HashMap<>();

	        for (Long customerId : pagedCustomerIds) {
	            List<CustomerProduct> products = customerProductRepository.findByCustomerIdWithProduct(customerId);

	            int qty = products.stream()
	                    .mapToInt(cp -> cp.getQuantity() != null ? cp.getQuantity() : 0)
	                    .sum();

	            int productCount = products.size();

	            double totalPrice = products.stream()
	                    .mapToDouble(cp -> {
	                        double price = (cp.getProduct() != null && cp.getProduct().getPrice() != null)
	                                ? cp.getProduct().getPrice().doubleValue()
	                                : 0.0;
	                        int quantity = (cp.getQuantity() != null) ? cp.getQuantity() : 0;
	                        return price * quantity;
	                    })
	                    .sum();

	            Map<String, Object> customerData = new HashMap<>();
	            customerData.put("products",
	                    products.stream().map(customerProductMapper::toResponse).collect(Collectors.toList()));
	            customerData.put("productCount", productCount);
	            customerData.put("totalPrice", totalPrice);

	            result.put(customerId, customerData);
	        }

	        response.put("data", result);
	        response.put("currentPage", page);
	        response.put("totalItems", customerIds.size());
	        response.put("totalPages", (customerIds.size() + size - 1) / size);

	        return ApiResponse.success("Customer-wise assignments fetched successfully", response);

	    } catch (Exception e) {
	        log.error("Error fetching customer-wise assignments with pagination: {}", e.getMessage(), e);
	        return ApiResponse.error("Failed to fetch customer-wise assignments");
	    }
	}

	@Override
	public Map<String, Object> getAllProductsWithPaggination(int page, int size) {
		Map<String, Object> response = new HashMap<>();
		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
			Page<Product> productPage = productRepository.findAll(pageable);

			List<ProductResponse> content = productPage.stream().map(productMapper::toResponse)
					.collect(Collectors.toList());

			response.put("data", content);
			response.put("currentPage", productPage.getNumber());
			response.put("totalItems", productPage.getTotalElements());
			response.put("totalPages", productPage.getTotalPages());
		} catch (Exception e) {
			log.error("Error fetching products with pagination: {}", e.getMessage(), e);
			response.put("error", "Failed to fetch products");
		}
		return response;
	}

	@Override
	public Object getAllCustomers() {
	    try {
	        List<Long> customerIds = customerProductRepository.findDistinctCustomerIds();
	        if (customerIds.isEmpty()) {
	            return Collections.emptyList();
	        }

	        RestTemplate restTemplate = new RestTemplate();
	        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
	            customerApiUrl,
	            HttpMethod.GET,
	            null,
	            new ParameterizedTypeReference<Map<String, Object>>() {}
	        );

	        List<String> legalNames = new ArrayList<>();
	        if (response.getBody() != null && response.getBody().get("data") instanceof List) {
	            List<Map<String, Object>> customerList = (List<Map<String, Object>>) response.getBody().get("data");
	            for (Map<String, Object> customer : customerList) {
	                if (customer.get("id") != null && customer.get("legalName") != null) {
	                    Long id = Long.parseLong(customer.get("id").toString());
	                    if (customerIds.contains(id)) {
	                        legalNames.add(customer.get("legalName").toString());
	                    }
	                }
	            }
	        }

	        return legalNames;

	    } catch (Exception e) {
	        log.error("Error fetching customer legal names: {}", e.getMessage(), e);
	        return Collections.singletonMap("error", "Failed to fetch customer legal names");
	    }
	}

	@Override
	public List<Map<String, Object>> getTop10ProductsByTotalAmount() {
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			List<Product> allProducts = productRepository.findAll();

			for (Product product : allProducts) {
				List<CustomerProduct> assignedList = customerProductRepository.findByProductId(product.getId());
				if (assignedList.isEmpty())
					continue;

				long totalCustomers = assignedList.stream().map(CustomerProduct::getCustomerId).distinct().count();

				int totalQuantity = assignedList.stream()
						.mapToInt(cp -> cp.getQuantity() != null ? cp.getQuantity() : 0).sum();

				BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(totalQuantity));

				Map<String, Object> map = new HashMap<>();
				map.put("productId", product.getId());
				map.put("productName", product.getName());
				map.put("category", product.getCategory());
				map.put("price", product.getPrice());
				map.put("currency", product.getCurrency());
				map.put("totalQuantity", totalQuantity);
				map.put("totalCustomers", totalCustomers);
				map.put("totalAmount", totalAmount);

				resultList.add(map);
			}

			return resultList.stream()
					.sorted((a, b) -> ((BigDecimal) b.get("totalAmount")).compareTo((BigDecimal) a.get("totalAmount")))
					.limit(10).collect(Collectors.toList());

		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public List<Map<String, Object>> getRecentCustomers(int limit) {
		try {
			LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
			List<CustomerProduct> allCustomers = customerProductRepository.findAll();

			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(customerApiUrl, HttpMethod.GET, null,
					new ParameterizedTypeReference<Map<String, Object>>() {
					});

			Map<Long, String> customerNameMap = new HashMap<>();
			if (response.getBody() != null && response.getBody().get("data") instanceof List) {
				List<Map<String, Object>> customerList = (List<Map<String, Object>>) response.getBody().get("data");
				for (Map<String, Object> customer : customerList) {
					if (customer.get("id") != null && customer.get("legalName") != null) {
						Long id = Long.parseLong(customer.get("id").toString());
						String name = customer.get("legalName").toString();
						customerNameMap.put(id, name);
					}
				}
			}

			return allCustomers.stream().filter(c -> c.getAssignedOn().isAfter(thirtyDaysAgo))
					.sorted((a, b) -> b.getAssignedOn().compareTo(a.getAssignedOn())).limit(limit).map(c -> {
						Map<String, Object> map = new HashMap<>();
						map.put("customerId", c.getCustomerId());
						map.put("customerName", customerNameMap.getOrDefault(c.getCustomerId(), "Unknown"));
						map.put("productName", productRepository.findById(c.getProductId()).map(Product::getName)
								.orElse("Unknown Product"));
						map.put("status", c.getStatus());
						map.put("price", productRepository.findById(c.getProductId()).map(Product::getPrice)
								.orElse(BigDecimal.ZERO));
						map.put("assignedOn", c.getAssignedOn());
						return map;
					}).collect(Collectors.toList());

		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
	
	@Override
	public Object getProductsByCustomerId(Long customerId) {
	    try {
	        List<CustomerProduct> customerProducts =
	                customerProductRepository.findByCustomerIdWithProduct(customerId);

	        if (customerProducts.isEmpty()) {
	            return Collections.emptyMap();
	        }

	        List<Map<String, Object>> products = customerProducts.stream().map(cp -> {
	            Map<String, Object> map = new HashMap<>();
	            map.put("productId", cp.getProduct().getId());
	            map.put("productName", cp.getProduct().getName());
	            map.put("category", cp.getProduct().getCategory());
	            map.put("price", cp.getProduct().getPrice());
	            map.put("currency", cp.getProduct().getCurrency());
	            map.put("description", cp.getProduct().getDescription());
	            map.put("quantity", cp.getQuantity());
	            map.put("status", cp.getStatus());
	            map.put("assignedOn", cp.getAssignedOn());
	            return map;
	        }).collect(Collectors.toList());

	        double totalPrice = customerProducts.stream()
	                .mapToDouble(cp -> cp.getProduct().getPrice().doubleValue() * cp.getQuantity())
	                .sum();

	        int totalQuantity = customerProducts.stream()
	                .mapToInt(CustomerProduct::getQuantity)
	                .sum();

	        Map<String, Object> response = new HashMap<>();
	        response.put("customerId", customerId);
	        response.put("totalQuantity", totalQuantity);
	        response.put("totalPrice", totalPrice);
	        response.put("products", products);

	        return response;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return Collections.emptyMap();
	    }
	}


}