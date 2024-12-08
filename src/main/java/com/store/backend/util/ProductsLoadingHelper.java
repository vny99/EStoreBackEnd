package com.store.backend.util;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.store.backend.dto.ProductDTO;
import com.store.backend.dto.ProductResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * Helper class for loading products
 */
@Component
@Slf4j
public class ProductsLoadingHelper {

	@Value("${products.load.url}")
	private String url;

	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private Validator validator;

	private static final String ERROR_LOADING_PRODUCTS = "Error loading products";	

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Load products
	 * 
	 * @return a List of productDTOs
	 */
	public Flux<ProductDTO> loadProducts() {

		return webClientBuilder
				.build()
				.get()
				.uri(url)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve()
				.bodyToMono(ProductResponse.class)
				.retry(3)
				.timeout(Duration.ofSeconds(5))
				.flatMapMany(response -> Flux.fromIterable(response.getProducts()))
				.map(this::validateProduct)
				.filter(Objects::nonNull)
				.onErrorContinue((e, product) -> {
		            log.error("Error processing product {}: {}", product, e.getMessage());
		        });
	}
	
	private ProductDTO validateProduct(ProductDTO product) {
	    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
	    if (!violations.isEmpty()) {
	        String errorMessages = violations.stream()
	                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
	                .collect(Collectors.joining(", "));
	        log.error("Validation failed for product: {}", errorMessages);
	        return null;
	    }
	    return product;
	}


}
