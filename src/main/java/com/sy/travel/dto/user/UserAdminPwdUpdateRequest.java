package com.sy.travel.dto.user;

import jakarta.validation.constraints.NotBlank;

public class UserAdminPwdUpdateRequest {
	@NotBlank(message = "operator不能为空")
	private String operator;
	@NotBlank(message = "password不能为空")
	private String password;
	@NotBlank(message = "newPwd不能为空")
	private String newPwd;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
}
