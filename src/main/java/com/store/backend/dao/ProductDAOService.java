package com.store.backend.dao;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.backend.repository.ProductRepository;

import reactor.core.publisher.Mono;

@Service
public class ProductDAOService {
	
	@Autowired ProductRepository productRepository;
	
	private static final Set<Long> productDAOIndex = new TreeSet<>();
	
	public Mono<ProductDAO> save(ProductDAO product) {
		if (productDAOIndex.contains(product.getId())) {
			product.setNew(false);
			return productRepository.save(product);
		} else {
			productDAOIndex.add(product.getId());
			return productRepository.save(product);
		}
       
    }

}
