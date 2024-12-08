package com.store.backend.dao;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_tags")
public class ProductTagDAO {

	private long productId;
	private String tag;
}
