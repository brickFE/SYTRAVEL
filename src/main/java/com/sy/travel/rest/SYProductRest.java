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
import com.sy.travel.service.SYProductService;
import com.sy.travel.utils.JSON;
/**
 * 产品模块的接口
 * @author liuxin
 *
 */
@RestController
@RequestMapping(value = "/sy/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class SYProductRest {
	@Autowired
	private SYProductService syProductService;

	/**
	 * 查看所有的产品信息
	 * 如果有名字传入就进行模糊查询
	 * @return
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> queryAll(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "1") int currentPage,
			@RequestParam(defaultValue = "10") int pageSize) {
		return AjaxResult.success(syProductService.findAll(name, currentPage, pageSize));
	}
	
	/**
	 * 点击团队时查看这个团队下的产品信息
	 */
	@RequestMapping(value =  "/teamid", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> queryByTeamId(@RequestParam("id") String id){
		return AjaxResult.success(syProductService.findByTeamId(id));
	}
	
	/**
	 * 点击团队时查看这个团队下的产品信息
	 */
	@RequestMapping(value =  "/classid", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> queryByClassesId(@RequestParam("id") String id){
		return AjaxResult.success(syProductService.findByClassId(id));
	}
	
	/**
	 * 添加产品信息
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> add(@RequestBody JSON json, HttpServletRequest request){
		return syProductService.add(json, request.getRemoteUser());
	}
	
	/**
	 * 删除产品信息
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public AjaxResult<String> delete(@RequestParam("id") int id, @RequestParam("operator") String operator, HttpServletRequest request){
		return syProductService.delete(id, operator);
	}
	
	/**
	 * 修改产品信息
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> update(@RequestBody JSON json, HttpServletRequest request){
		return syProductService.update(json, request.getRemoteUser());
	}
}
