package com.sy.travel.service.support;

import java.util.Date;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.common.ResultBuilder;
import com.sy.travel.entity.Logger;
import com.sy.travel.service.SYLoggerService;

public final class OperationResultSupport {

	private OperationResultSupport() {
	}

	public static AjaxResult<String> build(SYLoggerService loggerService, String operator, Date start, String reason,
			String status, String operation) {
		Logger logger = ResultBuilder.operationLogger(operator, start, reason, status, operation);
		loggerService.save(logger);
		return ResultBuilder.byStatus(status, reason);
	}
}
