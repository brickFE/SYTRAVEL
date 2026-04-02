package com.sy.travel.rest;

import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.dto.product.ProductCreateRequest;
import com.sy.travel.dto.product.ProductUpdateRequest;
import com.sy.travel.service.SYProductService;
/**
 * 产品模块的接口
 * @author liuxin
 *
 */
@RestController
@Validated
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
			@RequestParam(defaultValue = "1") @Min(1) int currentPage,
			@RequestParam(defaultValue = "10") @Min(1) int pageSize) {
		return AjaxResult.success(syProductService.findAll(name, currentPage, pageSize));
	}
	
	/**
	 * 点击团队时查看这个团队下的产品信息
	 */
	@RequestMapping(value =  "/teamid", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> queryByTeamId(@RequestParam("id") @Min(1) Integer id){
		return AjaxResult.success(syProductService.findByTeamId(String.valueOf(id)));
	}
	
	/**
	 * 点击团队时查看这个团队下的产品信息
	 */
	@RequestMapping(value =  "/classid", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> queryByClassesId(@RequestParam("id") @Min(1) Integer id){
		return AjaxResult.success(syProductService.findByClassId(String.valueOf(id)));
	}
	
	/**
	 * 添加产品信息
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> add(@Valid @RequestBody ProductCreateRequest request){
		return syProductService.add(request);
	}
	
	/**
	 * 删除产品信息
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public AjaxResult<String> delete(@RequestParam("id") @Min(1) int id, @RequestParam("operator") @NotBlank String operator){
		return syProductService.delete(id, operator);
	}
	
	/**
	 * 修改产品信息
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> update(@Valid @RequestBody ProductUpdateRequest request){
		return syProductService.update(request);
	}
}
