package com.sy.travel.service.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sy.travel.common.Commons;
import com.sy.travel.dao.SYUserRepository;
import com.sy.travel.entity.User;

@Component
public class PermissionGuard {

	@Autowired
	private SYUserRepository syUserRepository;

	public String requireAdmin(String operator) {
		User user = syUserRepository.findByUsername(operator);
		if (user == null) {
			return Commons.USER_OPERATOR_NOT_EXISTS;
		}
		return "0".equals(user.getPermission()) ? "" : Commons.USER_OPERATOR_NOT_PERMISSION;
	}
}
