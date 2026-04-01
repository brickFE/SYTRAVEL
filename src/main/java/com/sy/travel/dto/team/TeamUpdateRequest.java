package com.sy.travel.dto.team;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TeamUpdateRequest {
	@NotBlank(message = "operator不能为空")
	private String operator;
	@NotNull(message = "id不能为空")
	private Integer id;
	private String name;
	private String projectId;
	private String valid;
	private String remark;

	public String getOperator() { return operator; }
	public void setOperator(String operator) { this.operator = operator; }
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getProjectId() { return projectId; }
	public void setProjectId(String projectId) { this.projectId = projectId; }
	public String getValid() { return valid; }
	public void setValid(String valid) { this.valid = valid; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
}
