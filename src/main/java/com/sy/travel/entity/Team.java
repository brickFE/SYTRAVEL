package com.sy.travel.entity;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


import com.sy.travel.common.SY;

/**
 * 团队信息的实体类
 * 
 * @author liuxin
 *
 */
@Entity
public class Team implements SY {
	@Id
	@GeneratedValue
	private Integer id;// 自增长ID也是团的ID，用来做主键使用
	private String name;// 团名称
	private String projectId;// 项目ID
	private String valid;// 是否有效，1有效,0无效
	private String remark;// 备注信息
	private String createdUser;// 创建团的用户
	private String createdDate;// 团队创建日期
	private String modifiedUser;// 修改团信息的用户
	private String modifiedDate;// 团队修改日期

	public Team() {
	}

	public Team(String name, String projectId, String valid, String remark, String createdUser) {
		this.name = name;
		this.projectId = projectId;
		this.valid = valid;
		this.remark = remark;
		this.createdUser = createdUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getModifiedUser() {
		return modifiedUser;
	}

	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return toMap().toString();
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<>();
		m.put("id", id);
		m.put("name", name);
		m.put("projectId", projectId);
		m.put("valid", valid);
		m.put("remark", remark);
		m.put("createdUser", createdUser);
		m.put("createdDate", createdDate);
		m.put("modifiedUser", modifiedUser);
		m.put("modifiedDate", modifiedDate);
		return m;
	}
}
