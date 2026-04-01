package com.sy.travel.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.service.SYClassesService;
import com.sy.travel.utils.JSON;

/**
 * 分类的对页面接口
 * @author liuxin
 *
 */
@RestController
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
			@RequestParam(defaultValue = "1") int currentPage,
			@RequestParam(defaultValue = "10") int pageSize){
		return AjaxResult.success(syClassesService.queryAll(name, currentPage, pageSize));
	}
	
	/**
	 * 添加分类信息
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> add(@RequestBody JSON json, HttpServletRequest request){
		return syClassesService.add(json, request.getRemoteUser());
	}
	
	/**
	 * 删除分类信息:如果这个分类下有产品信息则不能删除这个分类
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public AjaxResult<String> delete(@RequestParam("id") int id, @RequestParam("operator") String operator, HttpServletRequest request){
		return syClassesService.delete(id, operator);
	}
	
	/**
	 * 修改分类信息
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> delete(@RequestBody JSON json, HttpServletRequest request) {
		return syClassesService.update(json, request.getRemoteUser());
	}
	
	/**
	 * 查看这个分类下的产品信息
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> info(@RequestParam("id") String id){
		return AjaxResult.success(syClassesService.info(id));
	}
}
