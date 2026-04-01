package com.sy.travel.dto.classes;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ClassesUpdateRequest {
	@NotBlank(message = "operator不能为空")
	private String operator;
	@NotNull(message = "id不能为空")
	private Integer id;
	private String name;
	private String sortId;
	private String parentId;
	private String remark;

	public String getOperator() { return operator; }
	public void setOperator(String operator) { this.operator = operator; }
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getSortId() { return sortId; }
	public void setSortId(String sortId) { this.sortId = sortId; }
	public String getParentId() { return parentId; }
	public void setParentId(String parentId) { this.parentId = parentId; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
}
