package com.sy.travel.dto.project;

import jakarta.validation.constraints.NotBlank;

public class ProjectCreateRequest {
	@NotBlank(message = "operator不能为空")
	private String operator;
	@NotBlank(message = "code不能为空")
	private String code;
	@NotBlank(message = "name不能为空")
	private String name;
	@NotBlank(message = "beginDate不能为空")
	private String beginDate;
	@NotBlank(message = "endDate不能为空")
	private String endDate;
	@NotBlank(message = "valid不能为空")
	private String valid;
	private String remark;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
