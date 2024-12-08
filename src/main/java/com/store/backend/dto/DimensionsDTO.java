package com.store.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DimensionsDTO {
	@NotNull(message = "Width is required.")
	@Positive(message = "Width must be greater than 0.")
	private Double width;

	@NotNull(message = "Height is required.")
	@Positive(message = "Height must be greater than 0.")
	private Double height;

	@NotNull(message = "Depth is required.")
	@Positive(message = "Depth must be greater than 0.")
	private Double depth;
}
