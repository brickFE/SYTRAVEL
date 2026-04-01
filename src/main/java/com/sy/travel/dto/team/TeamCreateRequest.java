package com.sy.travel.dto.team;

import javax.validation.constraints.NotBlank;

public class TeamCreateRequest {
	@NotBlank(message = "operator不能为空")
	private String operator;
	@NotBlank(message = "name不能为空")
	private String name;
	@NotBlank(message = "projectId不能为空")
	private String projectId;
	@NotBlank(message = "valid不能为空")
	private String valid;
	private String remark;

	public String getOperator() { return operator; }
	public void setOperator(String operator) { this.operator = operator; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getProjectId() { return projectId; }
	public void setProjectId(String projectId) { this.projectId = projectId; }
	public String getValid() { return valid; }
	public void setValid(String valid) { this.valid = valid; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
}
