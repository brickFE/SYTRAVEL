package com.sy.travel.dto.classes;

import jakarta.validation.constraints.NotBlank;

public class ClassesCreateRequest {
	@NotBlank(message = "operator不能为空")
	private String operator;
	@NotBlank(message = "name不能为空")
	private String name;
	@NotBlank(message = "sortId不能为空")
	private String sortId;
	private String parentId;
	private String remark;

	public String getOperator() { return operator; }
	public void setOperator(String operator) { this.operator = operator; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getSortId() { return sortId; }
	public void setSortId(String sortId) { this.sortId = sortId; }
	public String getParentId() { return parentId; }
	public void setParentId(String parentId) { this.parentId = parentId; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
}
