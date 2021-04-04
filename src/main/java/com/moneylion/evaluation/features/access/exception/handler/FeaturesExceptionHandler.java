package com.moneylion.evaluation.features.access.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.moneylion.evaluation.features.access.exception.FeatureAccessModificationException;
import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;

import lombok.AllArgsConstructor;
import lombok.Data;

@ControllerAdvice
public class FeaturesExceptionHandler {

	@AllArgsConstructor
	@Data
	class GenericAPIError {
		private Date timestamp;
		int status;
		String error;
		private String message;
		private String path;
	}

	@ExceptionHandler(FeatureNotFoundException.class)
	public final ResponseEntity<GenericAPIError> featureNotFoundException(FeatureNotFoundException excp,
			WebRequest request) {
		HttpStatus returnStatus = HttpStatus.NOT_FOUND;
		return getCommonErrorResponseEntity(returnStatus, excp, request);
	}

	@ExceptionHandler(FeatureAccessModificationException.class)
	public final ResponseEntity<GenericAPIError> featureAccessModificationException(
			FeatureAccessModificationException excp, WebRequest request) {
		HttpStatus returnStatus = HttpStatus.NOT_MODIFIED;
		return getCommonErrorResponseEntity(returnStatus, excp, request);
	}

	@ExceptionHandler(RuntimeException.class)
	public final ResponseEntity<GenericAPIError> handleAllExceptions(RuntimeException excp, WebRequest request) {
		HttpStatus returnStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		return getCommonErrorResponseEntity(returnStatus, excp, request);
	}

	private ResponseEntity<GenericAPIError> getCommonErrorResponseEntity(HttpStatus returnStatus, Throwable excp,
			WebRequest request) {
		return new ResponseEntity<GenericAPIError>(new GenericAPIError(new Date(), returnStatus.value(),
				returnStatus.getReasonPhrase(), excp.getLocalizedMessage(), request.getDescription(false)),
				returnStatus);
	}
}