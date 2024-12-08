package com.store.backend.dao;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_images")
public class ProductImageDAO{

	private long productId;
	private String imageUrl;
}
