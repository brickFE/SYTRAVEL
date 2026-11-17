package com.sy.travel.entity;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import com.sy.travel.common.SY;

/**
 * 项目的实体类
 * 
 * @author liuxin
 * 
 */
@Entity
public class Project implements SY{
	@Id
	@GeneratedValue
	private Integer id;// 项目id，自动生成，自动++
	private String code;// 项目编号，格式：SY-20180124-CN-BJ-001，必填
	private String name;// 项目名称，必填
	private String beginDate;// 项目开始时间
	private String endDate;// 项目结束时间
	private String valid;// 项目启用状态：0禁用，1启用，必填
	private String remark;// 项目的描述信息
	private String createdUser;// 创建者
	private String createdDate;// 创建时间
	private String modifiedUser;// 修改者
	private String modifiedDate;// 修改时间

	public Project() {
	}

	public Project(String code, String name, String beginDate, String endDate, String valid, String remark,
			String createdUser, String createdDate) {
		this.code = code;
		this.name = name;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.valid = valid;
		this.remark = remark;
		this.createdUser = createdUser;
		this.createdDate = createdDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedUser() {
		return modifiedUser;
	}

	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public String toString() {
		return toMap().toString();
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", id);
		m.put("code", code);
		m.put("name", name);
		m.put("beginDate", beginDate);
		m.put("endDate", endDate);
		m.put("valid", valid);
		m.put("remark", remark);
		m.put("createdUser", createdUser);
		m.put("createdDate", createdDate);
		m.put("modifiedUser", modifiedUser);
		m.put("modifiedDate", modifiedDate);
		return m;
	}

}
