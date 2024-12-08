package com.store.backend.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.store.backend.dao.ReviewDAO;
import com.store.backend.dto.ProductDTO;

import reactor.core.publisher.Flux;

@Repository
public interface ProductReviewRepository extends R2dbcRepository<ReviewDAO, Long>{

	Flux<ReviewDAO> findByProductId(Long id);

}
