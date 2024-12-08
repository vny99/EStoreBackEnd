package com.store.backend.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.store.backend.dao.ProductDAO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends R2dbcRepository<ProductDAO, Long> {

	Flux<ProductDAO> findByCategory(String category);

	Flux<ProductDAO> findAll(Sort sort);

	Mono<ProductDAO> findBySku(String sku);
}
