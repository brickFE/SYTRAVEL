package com.sy.travel.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.common.Commons;
import com.sy.travel.common.PasswordSupport;
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
			return AjaxResult.failed(200, reason);
		}
		if(StringUtils.isBlank(password)) {
			reason = Commons.LOGIN_CHECK_PWD_NOT_NULl;
			return AjaxResult.failed(200, reason);
		}
		User user = syUserRepository.findByUsername(username);
		if(user == null) {
			reason = Commons.LOGIN_CHECK_NAME_NOT_EXISTS;
			return AjaxResult.failed(200, reason);
		}
		if(!PasswordSupport.matches(user.getUsername(), password, user.getEncodedPassword())) {
			reason = Commons.LOGIN_CHECK_PWD_ERROR;
			return AjaxResult.failed(200, reason);
		}
		if(!PasswordSupport.isBcryptHash(user.getEncodedPassword())) {
			user.setPassword(PasswordSupport.hash(password));
			syUserRepository.save(user);
		}
		return AjaxResult.success(user.getPermission());
	}
}
