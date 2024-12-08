package com.store.backend.dao;

import java.time.Instant;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDAO {

	@Column("product_id")
    private Long productId;
	
	private Double rating;

	private String comment;

	private Instant date;

	@Column("reviewer_name")
	private String reviewerName;

	@Column("reviewer_email")
	private String reviewerEmail;

}
