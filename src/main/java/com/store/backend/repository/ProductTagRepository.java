package com.store.backend.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.store.backend.dao.ProductTagDAO;
import com.store.backend.dto.ProductDTO;

import reactor.core.publisher.Flux;

@Repository
public interface ProductTagRepository extends R2dbcRepository<ProductTagDAO, Long>{

	Flux<ProductTagDAO> findByProductId(Long id);

}
