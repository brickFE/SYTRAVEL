package com.sy.travel.common;

public final class ResultBuilder {
	private ResultBuilder() {
	}

	public static AjaxResult<String> byStatus(String status, String reason) {
		return "1".equals(status) ? AjaxResult.success(reason) : AjaxResult.failed(200, reason);
	}
}
