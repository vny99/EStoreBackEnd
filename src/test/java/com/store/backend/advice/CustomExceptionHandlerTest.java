package com.store.backend.advice;

import com.store.backend.exception.ProductNotFoundException;
import com.store.backend.exception.ProductServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import reactor.core.publisher.Mono;
import java.util.Collections;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomExceptionHandlerTest {
	private final CustomExceptionHandler exceptionHandler = new CustomExceptionHandler();

	@Test
	void testWrapSuccessResponseWithObject() {
		ResponseEntity<Map<String, Object>> response = exceptionHandler.wrapSuccessResponse(Map.of("key", "value"),
				HttpStatus.OK, "Success");
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Map<String, Object> body = response.getBody();
		assertNotNull(body);
		assertEquals("Success", body.get("message"));
		assertEquals(HttpStatus.OK, body.get("status"));
		assertEquals(Map.of("key", "value"), body.get("data"));
	}

	@Test
	void testWrapSuccessResponseWithoutObject() {
		ResponseEntity<Map<String, Object>> response = exceptionHandler.wrapSuccessResponse(null, HttpStatus.OK,
				"No Data");
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Map<String, Object> body = response.getBody();
		assertNotNull(body);
		assertEquals("No Data", body.get("message"));
		assertEquals(HttpStatus.OK, body.get("status"));
	}

	@Test
	void testWrapErrorResponseWithStringMessage() {
		ResponseEntity<Map<String, Object>> response = exceptionHandler.wrapErrorResponse("Error Occurred",
				HttpStatus.BAD_REQUEST);
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Map<String, Object> body = response.getBody();
		assertNotNull(body);
		assertEquals("Error Occurred", body.get("message"));
		assertEquals(HttpStatus.BAD_REQUEST, body.get("status"));
	}

	@Test
	void testWrapErrorResponseWithObjectMessage() {
		Map<String, String> errorDetails = Map.of("field", "Invalid value");
		ResponseEntity<Map<String, Object>> response = exceptionHandler.wrapErrorResponse(errorDetails,
				HttpStatus.BAD_REQUEST);
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Map<String, Object> body = response.getBody();
		assertNotNull(body);
		assertEquals(errorDetails, body.get("message"));
		assertEquals(HttpStatus.BAD_REQUEST, body.get("status"));
	}

	@Test
	void testHandleProductNotFoundException() {
		ProductNotFoundException exception = new ProductNotFoundException("Product not found");
		Mono<ResponseEntity<Map<String, Object>>> responseMono = exceptionHandler
				.handleProductNotFoundException(exception);
		ResponseEntity<Map<String, Object>> response = responseMono.block();
		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		Map<String, Object> body = response.getBody();
		assertNotNull(body);
		assertEquals("Product not found", body.get("message"));
		assertEquals(HttpStatus.NOT_FOUND, body.get("status"));
	}

	@Test
	void handleProductServiceException_ShouldReturnInternalServerErrorResponse() {
		String errorMessage = "An error occurred in the Product Service.";
		Throwable cause = new RuntimeException("Underlying cause of the error");
		ProductServiceException exception = new ProductServiceException(errorMessage, cause);
		Mono<ResponseEntity<Map<String, Object>>> responseMono = exceptionHandler
				.handleProductServiceException(exception);
		assertNotNull(responseMono);
		ResponseEntity<Map<String, Object>> response = responseMono.block();
		assertNotNull(response);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		Map<String, Object> responseBody = response.getBody();
		assertNotNull(responseBody);
		assertEquals("500 INTERNAL_SERVER_ERROR", responseBody.get("status").toString());
		assertEquals(errorMessage, responseBody.get("message"));
	}

	@Test
	void handleMethodArgumentNotValidException_ShouldReturnBadRequestResponse() {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		FieldError fieldError = new FieldError("objectName", "field", "error message");
		Mockito.when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
		MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
		ResponseEntity<Map<String, Object>> response = exceptionHandler
				.handleMethodArgumentNotValidException(exception);
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Map<String, Object> responseBody = response.getBody();
		assertNotNull(responseBody);
		Object statusValue = responseBody.get("status");
		assertNotNull(statusValue);
		assertEquals(HttpStatus.BAD_REQUEST.toString(), statusValue.toString());
		Map<String, String> errors = (Map<String, String>) responseBody.get("message");
		assertNotNull(errors);
		assertEquals("error message", errors.get("field"));
	}
}