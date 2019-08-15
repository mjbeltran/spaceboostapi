package com.mbg.spaceboostapi.exception;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ InvalidRequestException.class })
	public ResponseEntity<ListErrors> handleInvalidRequest(RuntimeException e, WebRequest request) {
		InvalidRequestException except = (InvalidRequestException) e;

		List<AttributeError> listAttErrors = except.getErrors().getFieldErrors().stream()
				.map(attributeError -> new AttributeError(attributeError.getObjectName(), attributeError.getField(),
						attributeError.getCode(), attributeError.getDefaultMessage()))
				.collect(Collectors.toList());

		ListErrors listErrors = new ListErrors(listAttErrors);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return new ResponseEntity<>(listErrors, headers, UNPROCESSABLE_ENTITY);
	}
}
