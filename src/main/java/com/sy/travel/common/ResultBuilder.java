package com.sy.travel.common;

import org.apache.commons.lang3.StringUtils;

public final class ResultBuilder {
	private ResultBuilder() {
	}

	public static AjaxResult<String> byStatus(String status, String reason) {
		return "1".equals(status) ? AjaxResult.success(reason) : AjaxResult.failed(200, reason);
	}

	public static String operationMessage(String operator, String operation, String reason) {
		return StringUtils.isBlank(reason) ? operator + operation + ":成功" : operator + operation + "失败原因:" + reason;
	}
}
