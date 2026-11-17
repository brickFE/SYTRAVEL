package com.sy.travel.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserCreateRequest {
	@NotBlank(message = "operator不能为空")
	private String operator;
	@NotBlank(message = "username不能为空")
	private String username;
	@NotBlank(message = "password不能为空")
	private String password;
	@NotBlank(message = "permission不能为空")
	@Pattern(regexp = "^[0-5]$", message = "permission取值范围为0-5")
	private String permission;
	private String remark;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
