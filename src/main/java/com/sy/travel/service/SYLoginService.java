package com.sy.travel.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.common.ApiMessages;
import com.sy.travel.common.Commons;
import com.sy.travel.dao.SYUserRepository;
import com.sy.travel.entity.User;
import com.sy.travel.utils.JSON;

/**
 * 登陆处理
 * @author liuxin
 *
 */
@Service
public class SYLoginService {
	@Autowired
	private SYUserRepository syUserRepository;
	
	public AjaxResult<String> loginCheck(JSON json) {
		String username = (String) json.get("username");
		String password = (String) json.get("password");
		String reason = "";
		if(StringUtils.isBlank(username)) {
			reason = Commons.LOGIN_CHECK_NAME_NOT_NULL;
			return returnResult(ApiMessages.FAILED, reason);
		}
		if(StringUtils.isBlank(password)) {
			reason = Commons.LOGIN_CHECK_PWD_NOT_NULl;
			return returnResult(ApiMessages.FAILED, reason);
		}
		User user = syUserRepository.findByUsername(username);
		if(user == null) {
			reason = Commons.LOGIN_CHECK_NAME_NOT_EXISTS;
			return returnResult(ApiMessages.FAILED, reason);
		}
		if(!password.equals(user.getPassword())) {
			reason = Commons.LOGIN_CHECK_PWD_ERROR;
			return returnResult(ApiMessages.FAILED, reason);
		}
		return returnResult(ApiMessages.SUCCESS,user.getPermission());
	}
	
	private AjaxResult<String> returnResult(String result, String reason) {
		if (ApiMessages.SUCCESS.equals(result)) {
			return AjaxResult.success(reason);
		}
		return AjaxResult.failed(200, reason);
	}
}
