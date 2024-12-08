package com.store.backend.service;



import com.store.backend.dto.ProductDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
	
	public Flux<ProductDTO> getProducts();
	
	public Mono<ProductDTO> getProductById(Long id);
	
	public Mono<ProductDTO> getProductBySku(String sku);
	
	public Flux<ProductDTO> getProductsByCategory(String category);

	public Flux<ProductDTO> getProductsSortedByPrice(String order);
	
	public Mono<String> loadProducts();

}
