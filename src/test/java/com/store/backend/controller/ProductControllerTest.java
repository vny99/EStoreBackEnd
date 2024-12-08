package com.store.backend.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.eq;

import com.store.backend.advice.CustomExceptionHandler;
import com.store.backend.dto.ProductDTO;
import com.store.backend.service.impl.ProductServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductControllerTest {

	@InjectMocks
	private ProductController productController;

	@Mock
	private ProductServiceImpl productServiceImpl;

	@Mock
	private CustomExceptionHandler customExceptionHandler;

	private static final String PRODUCT_ID = "1";
	private static final String SKU = "ABC123";
	private static final String CATEGORY = "electronics";
	private static final String SORT_ORDER = "asc";

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetProducts() {
		List<ProductDTO> mockProducts = Arrays.asList(new ProductDTO(), new ProductDTO());
		Map<String, Object> response = new HashMap<>();
		response.put("data", mockProducts);
		response.put("status", HttpStatus.OK);

		when(productServiceImpl.getProducts()).thenReturn(Flux.fromIterable(mockProducts));
		when(customExceptionHandler.wrapSuccessResponse(any(), eq(HttpStatus.OK), anyString()))
				.thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

		Mono<ResponseEntity<Map<String, Object>>> result = productController.getProducts();

		assertNotNull(result);
		ResponseEntity<Map<String, Object>> actual = result.block();
		assertEquals(HttpStatus.OK, actual.getStatusCode());
		assertEquals(mockProducts, actual.getBody().get("data"));
	}

	@Test
	public void testGetProductById() {
		ProductDTO mockProduct = new ProductDTO();
		Map<String, Object> response = new HashMap<>();
		response.put("data", mockProduct);
		response.put("status", HttpStatus.OK);

		when(productServiceImpl.getProductById(Long.valueOf(PRODUCT_ID))).thenReturn(Mono.just(mockProduct));
		when(customExceptionHandler.wrapSuccessResponse(any(), eq(HttpStatus.OK), anyString()))
				.thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

		Mono<ResponseEntity<Map<String, Object>>> result = productController.getProductById(PRODUCT_ID);

		assertNotNull(result);
		ResponseEntity<Map<String, Object>> actual = result.block();
		assertEquals(HttpStatus.OK, actual.getStatusCode());
		assertEquals(mockProduct, actual.getBody().get("data"));
	}

	@Test
	public void testGetProductBySku() {
		ProductDTO mockProduct = new ProductDTO();
		Map<String, Object> response = new HashMap<>();
		response.put("data", mockProduct);
		response.put("status", HttpStatus.OK);

		when(productServiceImpl.getProductBySku(SKU)).thenReturn(Mono.just(mockProduct));
		when(customExceptionHandler.wrapSuccessResponse(any(), eq(HttpStatus.OK), anyString()))
				.thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

		Mono<ResponseEntity<Map<String, Object>>> result = productController.getProductBySku(SKU);

		assertNotNull(result);
		ResponseEntity<Map<String, Object>> actual = result.block();
		assertEquals(HttpStatus.OK, actual.getStatusCode());
		assertEquals(mockProduct, actual.getBody().get("data"));
	}

	@Test
	public void testGetProductsByCategory() {
		List<ProductDTO> mockProducts = Arrays.asList(new ProductDTO(), new ProductDTO());
		Map<String, Object> response = new HashMap<>();
		response.put("data", mockProducts);
		response.put("status", HttpStatus.OK);

		when(productServiceImpl.getProductsByCategory(CATEGORY)).thenReturn(Flux.fromIterable(mockProducts));
		when(customExceptionHandler.wrapSuccessResponse(any(), eq(HttpStatus.OK), anyString()))
				.thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

		Mono<ResponseEntity<Map<String, Object>>> result = productController.getProductsByCategory(CATEGORY);

		assertNotNull(result);
		ResponseEntity<Map<String, Object>> actual = result.block();
		assertEquals(HttpStatus.OK, actual.getStatusCode());
		assertEquals(mockProducts, actual.getBody().get("data"));
	}

	@Test
	public void testGetProductsSortedByPrice() {
		List<ProductDTO> mockProducts = Arrays.asList(new ProductDTO(), new ProductDTO());
		Map<String, Object> response = new HashMap<>();
		response.put("data", mockProducts);
		response.put("status", HttpStatus.OK);

		when(productServiceImpl.getProductsSortedByPrice(SORT_ORDER)).thenReturn(Flux.fromIterable(mockProducts));
		when(customExceptionHandler.wrapSuccessResponse(any(), eq(HttpStatus.OK), anyString()))
				.thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

		Mono<ResponseEntity<Map<String, Object>>> result = productController.getProductsSortedByPrice(SORT_ORDER);

		assertNotNull(result);
		ResponseEntity<Map<String, Object>> actual = result.block();
		assertEquals(HttpStatus.OK, actual.getStatusCode());
		assertEquals(mockProducts, actual.getBody().get("data"));
	}

	@Test
	public void testLoadProducts() {
		when(productServiceImpl.loadProducts()).thenReturn(Mono.empty());
		when(customExceptionHandler.wrapSuccessResponse(Mockito.isNull(), eq(HttpStatus.OK), anyString()))
				.thenReturn(new ResponseEntity<>(HttpStatus.OK));

		Mono<ResponseEntity<Map<String, Object>>> result = productController.loadProducts();

		assertNotNull(result);
		ResponseEntity<Map<String, Object>> actual = result.block();
		assertEquals(HttpStatus.OK, actual.getStatusCode());
	}
}
