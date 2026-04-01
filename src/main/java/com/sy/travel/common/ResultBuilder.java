package com.sy.travel.common;

import org.apache.commons.lang3.StringUtils;

import com.sy.travel.entity.Logger;

import java.util.Date;

public final class ResultBuilder {
	private ResultBuilder() {
	}

	public static AjaxResult<String> byStatus(String status, String reason) {
		return "1".equals(status) ? AjaxResult.success(reason) : AjaxResult.failed(200, reason);
	}

	public static String operationMessage(String operator, String operation, String reason) {
		return StringUtils.isBlank(reason) ? operator + operation + ":成功" : operator + operation + "失败原因:" + reason;
	}

	public static Logger operationLogger(String operator, Date start, String reason, String status, String operation) {
		return new Logger(operator, DateFormat.sdf.format(start), DateFormat.sdf.format(new Date()),
				operationMessage(operator, operation, reason), status, operation);
	}
}
