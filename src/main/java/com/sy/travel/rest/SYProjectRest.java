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
import com.sy.travel.dto.project.ProjectCreateRequest;
import com.sy.travel.dto.project.ProjectUpdateRequest;
import com.sy.travel.service.SYProjectService;

/**
 * 项目模块的接口
 * 
 * @author liuxin
 *
 */
@RestController
@Validated
@RequestMapping(value = "/sy/project", produces = MediaType.APPLICATION_JSON_VALUE)
public class SYProjectRest {
	@Autowired
	private SYProjectService syProjectService;

	/**
	 * 查看所有项目的信息的接口
	 * 根据项目名称模糊查询这个项目的信息
	 * 当项目名称为空时，返回的是所有项目的信息
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> all(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "1") @Min(1) int currentPage,
			@RequestParam(defaultValue = "10") @Min(1) int pageSize
			) {
		Map<String, Object> data = syProjectService.queryAll(name, currentPage, pageSize);
		return AjaxResult.success(data);
	}

	/**
	 * 查询这个项目的详细信息
	 * 这个项目下的团队信息以及产品信息
	 * @return
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> info(@RequestParam(defaultValue = "1") @Min(1) Integer id) {
		return AjaxResult.success(syProjectService.info(id));
	}
	/**
	 * 添加项目信息的接口
	 * @param json		以json的形式传入；需要加入@RequestBody
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> add(@Valid @RequestBody ProjectCreateRequest request) {
		return syProjectService.add(request);
	}
	
	/**
	 * 删除项目信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public AjaxResult<String> delete(@RequestParam("id") @Min(1) Integer id, @RequestParam("operator") @NotBlank String operator){
		return syProjectService.deleteById(id, operator);
	}
	
	/**
	 * 根据id更新这个项目的信息
	 * @param json
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> update(@Valid @RequestBody ProjectUpdateRequest request){
		return syProjectService.update(request);
	}
}
