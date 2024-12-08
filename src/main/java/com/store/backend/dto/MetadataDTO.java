package com.store.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDTO {

	@NotBlank(message = "Creation date cannot be empty.")
	private String createdAt;

	@NotBlank(message = "Update date cannot be empty.")
	private String updatedAt;

	@NotBlank(message = "Barcode is required.")
	private String barcode;

	@NotBlank(message = "QR code URL cannot be empty.")
	private String qrCode;
}
