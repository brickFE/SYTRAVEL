package com.sy.travel.rest;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.dto.classes.ClassesCreateRequest;
import com.sy.travel.dto.classes.ClassesUpdateRequest;
import com.sy.travel.service.SYClassesService;

/**
 * 分类的对页面接口
 * @author liuxin
 *
 */
@RestController
@Validated
@RequestMapping(value = "/sy/classes", produces = MediaType.APPLICATION_JSON_VALUE)
public class SYClassesRest {
	@Autowired
	private SYClassesService syClassesService;
	/**
	 * 查看所有分类的信息
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> queryAll(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "1") @Min(1) int currentPage,
			@RequestParam(defaultValue = "10") @Min(1) int pageSize){
		return AjaxResult.success(syClassesService.queryAll(name, currentPage, pageSize));
	}
	
	/**
	 * 添加分类信息
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> add(@Valid @RequestBody ClassesCreateRequest request){
		return syClassesService.add(request);
	}
	
	/**
	 * 删除分类信息:如果这个分类下有产品信息则不能删除这个分类
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public AjaxResult<String> delete(@RequestParam("id") @Min(1) int id, @RequestParam("operator") @NotBlank String operator){
		return syClassesService.delete(id, operator);
	}
	
	/**
	 * 修改分类信息
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> update(@Valid @RequestBody ClassesUpdateRequest request) {
		return syClassesService.update(request);
	}
	
	/**
	 * 查看这个分类下的产品信息
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> info(@RequestParam("id") @Min(1) Integer id){
		return AjaxResult.success(syClassesService.info(String.valueOf(id)));
	}
}
