package com.moneylion.evaluation.features.access.exception.handler;

import java.util.Date;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.moneylion.evaluation.features.access.exception.FeatureAccessModificationException;
import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.exception.FeatureUserNotFoundException;

import lombok.AllArgsConstructor;
import lombok.Data;

@ControllerAdvice
public class FeaturesAccessServiceExceptionHandler {

	@AllArgsConstructor
	@Data
	class GenericAPIError {
		private Date timestamp;
		int status;
		String error;
		private String message;
		private String path;
	}

	@ExceptionHandler({ FeatureNotFoundException.class, FeatureUserNotFoundException.class })
	public final ResponseEntity<GenericAPIError> Exception(Exception excp, WebRequest request) {
		HttpStatus returnStatus = HttpStatus.NOT_FOUND;
		return getCommonErrorResponseEntity(returnStatus, request, () -> excp.getLocalizedMessage());
	}

	@ExceptionHandler(FeatureAccessModificationException.class)
	public final ResponseEntity<GenericAPIError> handleFeatureAccessModificationException(
			FeatureAccessModificationException excp, WebRequest request) {
		HttpStatus returnStatus = HttpStatus.NOT_MODIFIED;
		return getCommonErrorResponseEntity(returnStatus, request, () -> excp.getLocalizedMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GenericAPIError> handleValidationExceptions(MethodArgumentNotValidException excp,
			WebRequest request) {
		HttpStatus returnStatus = HttpStatus.BAD_REQUEST;
		return getCommonErrorResponseEntity(returnStatus, request, () -> excp.getBindingResult().getAllErrors().stream()
				.map(FieldError.class::cast).map(FieldError::getDefaultMessage).collect(Collectors.toSet()).toString());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<GenericAPIError> handleValidationExceptions(ConstraintViolationException excp,
			WebRequest request) {
		HttpStatus returnStatus = HttpStatus.BAD_REQUEST;
		return getCommonErrorResponseEntity(returnStatus, request, () -> excp.getConstraintViolations().stream()
				.map(ConstraintViolation::getMessage).collect(Collectors.toSet()).toString());
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class, ServletRequestBindingException.class })
	public ResponseEntity<GenericAPIError> handleBadOrMalformedRequestExceptions(Exception excp, WebRequest request) {
		HttpStatus returnStatus = HttpStatus.BAD_REQUEST;
		return getCommonErrorResponseEntity(returnStatus, request, () -> excp.getLocalizedMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<GenericAPIError> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException excp, WebRequest request) {
		HttpStatus returnStatus = HttpStatus.METHOD_NOT_ALLOWED;
		return getCommonErrorResponseEntity(returnStatus, request, () -> excp.getLocalizedMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public final ResponseEntity<GenericAPIError> handleRuntimeExceptions(RuntimeException excp, WebRequest request) {
		HttpStatus returnStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		return getCommonErrorResponseEntity(returnStatus, request,
				() -> "Server ran into an error while processing this request.");
	}

	@ExceptionHandler(Throwable.class)
	public final ResponseEntity<GenericAPIError> handleAllOtherExceptions(Throwable excp, WebRequest request) {
		HttpStatus returnStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		return getCommonErrorResponseEntity(returnStatus, request,
				() -> "Server ran into an error while processing this request.");
	}

	private ResponseEntity<GenericAPIError> getCommonErrorResponseEntity(HttpStatus returnStatus, WebRequest request,
			Supplier<String> errorMessageSupplier) {
		return new ResponseEntity<GenericAPIError>(new GenericAPIError(new Date(), returnStatus.value(),
				returnStatus.getReasonPhrase(), errorMessageSupplier.get(), request.getDescription(false)),
				returnStatus);
	}
}