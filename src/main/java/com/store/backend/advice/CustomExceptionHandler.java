package com.store.backend.advice;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.store.backend.exception.ProductNotFoundException;
import com.store.backend.exception.ProductServiceException;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class CustomExceptionHandler {

	private static final String MESSAGE = "message";
	private static final String STATUS = "status";
	private static final String DATA = "data";

	@ResponseBody
	public ResponseEntity<Map<String, Object>> wrapSuccessResponse(Object obj, HttpStatus status, String message) {
		Map<String, Object> response;
		if (obj != null) {
			response = Map.of(STATUS, status, MESSAGE, message, DATA, obj);

		} else {
			response = Map.of(STATUS, status, MESSAGE, message);
		}
		return ResponseEntity.status(status).body(response);
	}

	 @ResponseBody
	    public ResponseEntity<Map<String, Object>> wrapErrorResponse(Object message, HttpStatus status) {
	        Map<String, Object> response;
	        if (message instanceof String) {
	            String messageString = (String) message;
	            response = Map.of(STATUS, status, MESSAGE, messageString);
	        } else {
	            response = Map.of(STATUS, status, MESSAGE, message);
	        }
	        return ResponseEntity.status(status).body(response);
	    }

	    @ExceptionHandler(ProductNotFoundException.class)
	    public Mono<ResponseEntity<Map<String, Object>>> handleProductNotFoundException(ProductNotFoundException ex) {
	        return Mono.just(this.wrapErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND));
	    }

	    @ExceptionHandler(ProductServiceException.class)
	    public Mono<ResponseEntity<Map<String, Object>>> handleProductServiceException(ProductServiceException ex) {
	        return Mono.just(this.wrapErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
	    }
	    
	    @ExceptionHandler(MethodArgumentNotValidException.class)
		@ResponseStatus(HttpStatus.BAD_REQUEST)
		public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
				MethodArgumentNotValidException ex) {
			Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			return this.wrapErrorResponse(errors, HttpStatus.BAD_REQUEST);
		}
}
