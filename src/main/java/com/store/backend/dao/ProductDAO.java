package com.store.backend.dao;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class ProductDAO implements Persistable<Long>{

	@Id
	private Long id;

	private String title;

	private String description;

	private String category;

	private Double price;

	@Column("discount_percentage")
	private Double discountPercentage;

	private Double rating;

	private Integer stock;

	private String brand;

	private String sku;

	private Double weight;

	@Column("width")
	private Double width;

	@Column("height")
	private Double height;

	@Column("depth")
	private Double depth;

	@Column("warranty_information")
	private String warrantyInformation;

	@Column("shipping_information")
	private String shippingInformation;

	@Column("availability_status")
	private String availabilityStatus;

	@Column("return_policy")
	private String returnPolicy;

	@Column("minimum_order_quantity")
	private Integer minimumOrderQuantity;

	@Column("created_at")
	private Instant createdAt;

	@Column("updated_at")
	private Instant updatedAt;

	@Column("barcode")
	private String barcode;

	@Column("qr_code")
	private String qrCode;

	private String thumbnail;
	
	@Transient
	private boolean isNew = true;
	
	 @Override
	    @Transient
	    public boolean isNew() {
	        return isNew;
	    }

}
