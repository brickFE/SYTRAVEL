package com.sy.travel.handler;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.sy.travel.common.AjaxResult;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public AjaxResult<String> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage())
				.collect(Collectors.joining(";"));
		return AjaxResult.validationError(message);
	}

	@ExceptionHandler({
			ConstraintViolationException.class,
			MissingServletRequestParameterException.class,
			MethodArgumentTypeMismatchException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public AjaxResult<String> handleBadRequest(Exception ex) {
		return AjaxResult.validationError(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public AjaxResult<String> handleUnexpected(Exception ex) {
		return AjaxResult.internalError(ex.getMessage());
	}
}
