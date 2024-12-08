package com.store.backend.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.store.backend.dao.ProductImageDAO;
import com.store.backend.dto.ProductDTO;

import reactor.core.publisher.Flux;

@Repository
public interface ProductImageRepository extends R2dbcRepository<ProductImageDAO, Long>{

	Flux<ProductImageDAO> findByProductId(Long id);

}
