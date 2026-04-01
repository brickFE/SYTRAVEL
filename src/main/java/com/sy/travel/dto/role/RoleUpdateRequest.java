package com.sy.travel.dto.role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RoleUpdateRequest {
	@NotBlank(message = "operator不能为空")
	private String operator;
	@NotNull(message = "id不能为空")
	private Integer id;
	private String role;
	private String email;
	private String mobile;
	private String remark;

	public String getOperator() { return operator; }
	public void setOperator(String operator) { this.operator = operator; }
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getMobile() { return mobile; }
	public void setMobile(String mobile) { this.mobile = mobile; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
}
