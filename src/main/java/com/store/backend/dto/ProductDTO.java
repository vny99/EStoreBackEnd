package com.store.backend.dto;

import java.util.List;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

	@NotNull(message = "Product ID is required.")
	private Long id;

	@NotBlank(message = "Product title cannot be empty.")
	@Size(max = 100, message = "Product title must not exceed 100 characters.")
	private String title;

	@NotBlank(message = "Product description cannot be empty.")
	@Size(max = 500, message = "Product description must not exceed 500 characters.")
	private String description;

	@NotBlank(message = "Category is required.")
	private String category;

	@NotNull(message = "Price is required.")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
	private Double price;

	@DecimalMin(value = "0.0", message = "Discount percentage must be 0 or higher.")
	@DecimalMax(value = "100.0", message = "Discount percentage must be 100 or less.")
	private Double discountPercentage;

	@DecimalMin(value = "0.0", message = "Rating must be 0 or higher.")
	@DecimalMax(value = "5.0", message = "Rating must be 5 or less.")
	private Double rating;

	@NotNull(message = "Stock quantity is required.")
	@Min(value = 0, message = "Stock cannot be negative.")
	private Integer stock;

	@NotEmpty(message = "Tags cannot be empty.")
	private List<@NotBlank(message = "Each tag cannot be empty.") String> tags;

	@NotBlank(message = "Brand is required.")
	@NotNull(message = "Brand is required.")
	private String brand;

	@NotBlank(message = "SKU is required.")
	private String sku;

	@NotNull(message = "Weight is required.")
	@Positive(message = "Weight must be greater than 0.")
	private Integer weight;

	@NotNull(message = "Dimensions are required.")
	private DimensionsDTO dimensions;

	@NotBlank(message = "Warranty information cannot be empty.")
	private String warrantyInformation;

	@NotBlank(message = "Shipping information cannot be empty.")
	private String shippingInformation;

	@NotBlank(message = "Availability status is required.")
	private String availabilityStatus;

	@NotNull(message = "Reviews are required.")
	private List<ReviewDTO> reviews;

	@NotBlank(message = "Return policy cannot be empty.")
	private String returnPolicy;

	@NotNull(message = "Minimum order quantity is required.")
	@Positive(message = "Minimum order quantity must be greater than 0.")
	private Integer minimumOrderQuantity;

	@NotNull(message = "Meta information is required.")
	private MetadataDTO meta;

	@NotEmpty(message = "At least one image is required.")
	private List<@NotBlank(message = "Image URL cannot be empty.") String> images;

	@NotBlank(message = "Thumbnail URL cannot be empty.")
	private String thumbnail;
}
