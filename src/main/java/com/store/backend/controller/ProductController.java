package com.store.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.backend.advice.CustomExceptionHandler;
import com.store.backend.service.ProductService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Controller for Products
 */
@RestController
@RequestMapping("/api/products")
@Slf4j
@CrossOrigin("*")
public class ProductController {

	@Autowired
	private ProductService productServiceImpl;
	@Autowired
	private CustomExceptionHandler customExceptionHandler;

	private static final String DEFAULT_ORDER = "asc";

	private static final String GETTING_PRODUCTS = "Get products endpoint called";
	private static final String GETTING_PRODUCT_BY_ID = "Get product by ID endpoint called with id: ";
	private static final String GETTING_PRODUCT_BY_SKU = "Get product by SKU endpoint called with sku: ";
	private static final String GETTING_PRODUCTS_BY_CATEGORY = "Get products by category endpoint called with category: ";
	private static final String GETTING_PRODUCTS_SORTED_BY_PRICE = "Get products sorted by price endpoint called with order: ";
	private static final String LOADING_PRODUCTS = "Load products endpoint called";
	
	private static final String PRODUCTS_FETCHED_SUCESSFULLY= "Products fetched successfully";
	private static final String PRODUCT_FETCHED_SUCESSFULLY= "Product fetched successfully";
	private static final String PRODUCTS_LOADED_SUCESSFULLY= "Products loaded successfully";



	/**
	 * Get all products
	 * 
	 * @return a Mono of ResponseEntity containing a Map with product data and
	 *         status
	 */
	@GetMapping()
	public Mono<ResponseEntity<Map<String, Object>>> getProducts() {
		log.info(GETTING_PRODUCTS);
		return productServiceImpl.getProducts().collectList().map(products -> customExceptionHandler
				.wrapSuccessResponse(products, HttpStatus.OK, PRODUCTS_FETCHED_SUCESSFULLY));
	}

	/**
	 * Get product by ID
	 * 
	 * @param id the ID of the product
	 * @return a Mono of ResponseEntity containing a Map with product data and
	 *         status
	 */
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Map<String, Object>>> getProductById(@PathVariable String id) {
		log.info(GETTING_PRODUCT_BY_ID + id);
		return productServiceImpl.getProductById(Long.valueOf(id)).map(product -> customExceptionHandler
				.wrapSuccessResponse(product, HttpStatus.OK, PRODUCT_FETCHED_SUCESSFULLY));
	}

	/**
	 * Get product by SKU
	 * 
	 * @param sku the SKU of the product
	 * @return a Mono of ResponseEntity containing a Map with product data and
	 *         status
	 */
	@GetMapping("/sku/{sku}")
	public Mono<ResponseEntity<Map<String, Object>>> getProductBySku(@PathVariable String sku) {
		log.info(GETTING_PRODUCT_BY_SKU + sku);
		return productServiceImpl.getProductBySku(sku).map(product -> customExceptionHandler
				.wrapSuccessResponse(product, HttpStatus.OK, PRODUCT_FETCHED_SUCESSFULLY));
	}

	/**
	 * Get products by category
	 * 
	 * @param category the category to filter products by
	 * @return a Mono of ResponseEntity containing a Map with product data and
	 *         status
	 */
	@GetMapping("/category/{category}")
	public Mono<ResponseEntity<Map<String, Object>>> getProductsByCategory(@PathVariable String category) {
		log.info(GETTING_PRODUCTS_BY_CATEGORY + category);
		return productServiceImpl.getProductsByCategory(category).collectList().map(products -> customExceptionHandler
				.wrapSuccessResponse(products, HttpStatus.OK, PRODUCT_FETCHED_SUCESSFULLY));
	}

	/**
	 * Get products sorted by price
	 * 
	 * @param order the order to sort products by (either "asc" or "desc")
	 * @return a Mono of ResponseEntity containing a Map with product data and
	 *         status
	 */
	@GetMapping("/sort/{order}")
	public Mono<ResponseEntity<Map<String, Object>>> getProductsSortedByPrice(@PathVariable String order) {
		log.info(GETTING_PRODUCTS_SORTED_BY_PRICE + order);
		return productServiceImpl.getProductsSortedByPrice(order).collectList().map(products -> customExceptionHandler
				.wrapSuccessResponse(products, HttpStatus.OK, PRODUCTS_FETCHED_SUCESSFULLY));
	}

	/**
	 * Load products
	 * 
	 * @return a Mono of ResponseEntity indicating the successful loading of
	 *         products
	 */
	@PostMapping("/load")
	public Mono<ResponseEntity<Map<String, Object>>> loadProducts() {
		log.info(LOADING_PRODUCTS);
		return productServiceImpl.loadProducts().then(Mono
				.just(customExceptionHandler.wrapSuccessResponse(null, HttpStatus.OK, PRODUCTS_LOADED_SUCESSFULLY)));
	}

}
