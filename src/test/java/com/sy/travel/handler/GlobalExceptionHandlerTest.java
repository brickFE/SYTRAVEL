package com.sy.travel.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.common.ApiMessages;
import com.sy.travel.common.ErrorCodes;

public class GlobalExceptionHandlerTest {

	@Test
	public void handleUnexpectedShouldHideInternalExceptionMessage() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		AjaxResult<String> result = handler.handleUnexpected(new RuntimeException("db-password-leak"));
		assertEquals(500, result.getCode());
		assertEquals(ErrorCodes.INTERNAL_ERROR, result.getMsg());
		assertEquals(ApiMessages.INTERNAL_ERROR_MESSAGE, result.getData());
	}
}
