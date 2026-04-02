package com.sy.travel.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.sy.travel.dto.role.RoleCreateRequest;
import com.sy.travel.dto.role.RoleUpdateRequest;
import com.sy.travel.service.SYRoleService;

/**
 * 角色模块接口
 * 
 * @author liuxin
 *
 */
@RestController
@Validated
@RequestMapping(value = "/sy/role", produces = MediaType.APPLICATION_JSON_VALUE)
public class SYRoleRest {
	@Autowired
	private SYRoleService syRoleService;

	/**
	 * 查看角色信息
	 * 下拉列表：查看所选团队下角色信息
	 * 
	 * @param teamId
	 * @return
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> queryAll(
			@RequestParam(defaultValue = "") String teamId,
			@RequestParam(defaultValue = "1") @Min(1) int currentPage,
			@RequestParam(defaultValue = "10") @Min(1) int pageSize) {
		return AjaxResult.success(syRoleService.queryAll(teamId, currentPage, pageSize));
	}

	/**
	 * 添加角色信息
	 * 
	 * @param json
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public AjaxResult<String> add(@Valid @RequestBody RoleCreateRequest request) {
		return syRoleService.add(request);
	}

	/**
	 * 删除角色信息
	 * @param id
	 * @param operator
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public AjaxResult<String> delete(@RequestParam("id") @Min(1) int id, @RequestParam("operator") @NotBlank String operator,
			HttpServletRequest request) {
		return syRoleService.delete(id, operator);
	}

	/**
	 * 修改角色信息
	 * @param json
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> update(@Valid @RequestBody RoleUpdateRequest request){
		return syRoleService.update(request);
	}
}
