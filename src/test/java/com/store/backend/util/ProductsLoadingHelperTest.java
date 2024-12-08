package com.store.backend.util;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.store.backend.dao.ReviewDAO;
import com.store.backend.dto.DimensionsDTO;
import com.store.backend.dto.MetadataDTO;
import com.store.backend.dto.ProductDTO;
import com.store.backend.dto.ProductResponse;
import com.store.backend.dto.ReviewDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ProductsLoadingHelperTest {

	@InjectMocks
	private ProductsLoadingHelper productsLoadingHelper;

	@Mock
	private WebClient.Builder webClientBuilder;

	@Mock
	private WebClient webClient;

	@Mock
	private WebClient.RequestBodyUriSpec requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
	
    private WebClient.RequestBodySpec requestBodySpec = mock(WebClient.RequestBodySpec.class);
    
    private WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
    
    private WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

	@Mock
	private Validator validator;

	private ProductDTO sampleProductDTO;
	private ReviewDTO sampleReviewDTO;
	private ReviewDAO sampleReviewDAO;
	private DimensionsDTO sampleDimensionsDTO;
	private MetadataDTO sampleMetadataDTO;

//	@Before
//	public void setUp() {
//		MockitoAnnotations.openMocks(this);
//
//		productsLoadingHelper.setUrl("https://www.example.com");
//
//		sampleDimensionsDTO = new DimensionsDTO(10.0, 20.0, 30.0);
//		sampleMetadataDTO = new MetadataDTO("2023-01-01", "2023-01-02", "123456789", "http://example.com/qr");
//		sampleReviewDTO = new ReviewDTO(4.5, "Great product!", "2023-01-05", "John Doe", "john.doe@example.com");
//		sampleReviewDAO = new ReviewDAO(1l, 4.5, "Great product!", Instant.now(), "John Doe", "john.doe@example.com");
//		sampleProductDTO = new ProductDTO(1L, "Product A", "Description A", "Category A", 100.0, 10.0, 4.0, 50,
//				Arrays.asList("tag1", "tag2"), "Brand A", "SKU123", 200, sampleDimensionsDTO, "Warranty info",
//				"Shipping info", "In Stock", Arrays.asList(sampleReviewDTO), "Return policy", 1, sampleMetadataDTO,
//				Arrays.asList("image1.jpg", "image2.jpg"), "thumbnail.jpg");
//
//		when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
//        when(webClientBuilder.build()).thenReturn(webClient);
//        
//        when(webClient.get()).thenReturn(requestHeadersSpec);
//
//		// Mock WebClient steps with consistent return types
//		Mockito.when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec<?>) requestHeadersUriSpec);
//		Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString()))
//				.thenReturn((WebClient.RequestHeadersUriSpec<?>) requestHeadersUriSpec);
//		Mockito.when(requestHeadersUriSpec.header(Mockito.eq(HttpHeaders.CONTENT_TYPE),
//				Mockito.eq(MediaType.APPLICATION_JSON_VALUE)))
//				.thenReturn((WebClient.RequestHeadersSpec<?>) requestHeadersSpec);
//		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//	}
//
//	@Test
//	public void testLoadProducts_success() {
//		// Mock ProductResponse
//		ProductResponse mockResponse = new ProductResponse();
//		ProductDTO product1 = new ProductDTO("Product1");
//		ProductDTO product2 = new ProductDTO("Product2");
//		mockResponse.setProducts(Arrays.asList(product1, product2));
//
//		// Mock Validator behavior
//		Mockito.when(validator.validate(Mockito.any(ProductDTO.class))).thenReturn(Collections.emptySet()); // No
//																											// violations
//
//		// Mock ResponseSpec behavior
//		Mockito.when(responseSpec.bodyToMono(ProductResponse.class)).thenReturn(Mono.just(mockResponse));
//
//		// Test the method
//		Flux<ProductDTO> result = productsLoadingHelper.loadProducts();
//
//		List<ProductDTO> products = result.collectList().block();
//		Assert.assertNotNull(products);
//		Assert.assertEquals(2, products.size());
//		Assert.assertEquals("Product1", products.get(0).getName());
//		Assert.assertEquals("Product2", products.get(1).getName());
//	}
//
//	@Test
//	public void testLoadProducts_validationError() {
//		// Mock ProductResponse
//		ProductResponse mockResponse = new ProductResponse();
//		ProductDTO product1 = new ProductDTO("Product1");
//		ProductDTO product2 = new ProductDTO("InvalidProduct");
//		mockResponse.setProducts(Arrays.asList(product1, product2));
//
//		// Mock Validator behavior
//		Mockito.when(validator.validate(product1)).thenReturn(Collections.emptySet()); // No violations
//		Mockito.when(validator.validate(product2))
//				.thenReturn(Collections.singleton(createConstraintViolation("name", "must not be null")));
//
//		// Mock ResponseSpec behavior
//		Mockito.when(responseSpec.bodyToMono(ProductResponse.class)).thenReturn(Mono.just(mockResponse));
//
//		// Test the method
//		Flux<ProductDTO> result = productsLoadingHelper.loadProducts();
//
//		List<ProductDTO> products = result.collectList().block();
//		Assert.assertNotNull(products);
//		Assert.assertEquals(1, products.size());
//		Assert.assertEquals("Product1", products.get(0).getName());
//	}
//
//	private ConstraintViolation<ProductDTO> createConstraintViolation(String propertyPath, String message) {
//		ConstraintViolation<ProductDTO> violation = Mockito.mock(ConstraintViolation.class);
//		Mockito.when(violation.getPropertyPath()).thenReturn(() -> propertyPath);
//		Mockito.when(violation.getMessage()).thenReturn(message);
//		return violation;
//	}
}
