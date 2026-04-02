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
import com.sy.travel.dto.user.UserAdminPwdUpdateRequest;
import com.sy.travel.dto.user.UserCreateRequest;
import com.sy.travel.dto.user.UserUpdateRequest;
import com.sy.travel.service.SYUserService;

/**
 * 用户模块接口
 * @author liuxin
 *
 */
@RestController
@Validated
@RequestMapping(value = "/sy/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class SYUserRest {
	@Autowired
	private SYUserService syUserservice;

	/**
	 * 查看所有用户信息
	 * 当name不为空时进行模糊查询
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> queryAll(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "1") @Min(1) int currentPage,
			@RequestParam(defaultValue = "10") @Min(1) int pageSize) {
		return AjaxResult.success(syUserservice.queryAll(name, currentPage, pageSize));
	}

	/**
	 * 添加用户信息
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> add(@Valid @RequestBody UserCreateRequest request) {
		return syUserservice.add(request);
	}

	/**
	 * 删除用户信息
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public AjaxResult<String> delete(@RequestParam("id") @Min(1) int id, @RequestParam("operator") @NotBlank String operator) {
		return syUserservice.delete(id, operator);
	}

	/**
	 * 修改用户信息
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> update(@Valid @RequestBody UserUpdateRequest request){
		return syUserservice.update(request);
	}
	
	/**
	 * 修改超级管理员密码
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/adminpwd", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> updatePwd(@Valid @RequestBody UserAdminPwdUpdateRequest request){
		return syUserservice.updateAdminPwd(request);
	}
	
	/**
	 * 通过用户名获得用户的权限
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/permission", method = RequestMethod.GET)
	public AjaxResult<String> permission(@RequestParam("name") String username) {
		return syUserservice.getPermission(username);
	}

}
