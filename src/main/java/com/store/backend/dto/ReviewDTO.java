package com.store.backend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

	@DecimalMin(value = "0.0", message = "Rating must be 0 or higher.")
	@DecimalMax(value = "5.0", message = "Rating must be 5 or less.")
	private Double rating;

	@NotBlank(message = "Review comment cannot be empty.")
	private String comment;

	@NotNull(message = "Review date is required.")
	private String date;

	@NotBlank(message = "Reviewer name cannot be empty.")
	private String reviewerName;

	@Email(message = "Invalid email format.")
	@NotBlank(message = "Reviewer email cannot be empty.")
	private String reviewerEmail;
}
