package com.sy.travel.dto.role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class RoleCreateRequest {
	@NotBlank(message = "operator不能为空")
	private String operator;
	@NotBlank(message = "name不能为空")
	private String name;
	@NotBlank(message = "teamId不能为空")
	private String teamId;
	@NotBlank(message = "role不能为空")
	private String role;
	@Pattern(regexp = "^$|^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",
			message = "邮箱格式错误")
	private String email;
	@NotBlank(message = "mobile不能为空")
	@Pattern(regexp = "^1[3|4|5|7|8][0-9]{9}$", message = "手机号格式错误")
	private String mobile;
	private String remark;

	public String getOperator() { return operator; }
	public void setOperator(String operator) { this.operator = operator; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getTeamId() { return teamId; }
	public void setTeamId(String teamId) { this.teamId = teamId; }
	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getMobile() { return mobile; }
	public void setMobile(String mobile) { this.mobile = mobile; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
}
