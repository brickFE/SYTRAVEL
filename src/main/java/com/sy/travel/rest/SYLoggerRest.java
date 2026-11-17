package com.sy.travel.rest;

import java.util.Map;

import jakarta.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sy.travel.common.AjaxResult;
import com.sy.travel.service.SYLoggerService;

/**
 * 日志记录查看的接口
 * 
 * @author liuxin
 *
 */
@RestController
@Validated
@RequestMapping(value = "/sy/logger", produces = MediaType.APPLICATION_JSON_VALUE)
public class SYLoggerRest {
	@Autowired
	private SYLoggerService syLoggerService;

	/**
	 * 查看所有的日志信息
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> queryAll(
			@RequestParam(defaultValue = "1") @Min(1) int currentPage,
			@RequestParam(defaultValue = "10") @Min(1) int pageSize
			){
		return AjaxResult.success(syLoggerService.all(currentPage, pageSize));
	}
}
