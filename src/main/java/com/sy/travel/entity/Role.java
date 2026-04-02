package com.sy.travel.entity;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import com.sy.travel.common.SY;
/**
 * 角色实体类
 * @author liuxin
 *
 */
@Entity
public class Role implements SY {
	@Id
	@GeneratedValue
	private Integer id;// 角色id
	private String name;//角色名,用户名字不可修改，如需修改直接删除
	private Integer teamId;// 团队id,所属团队不能修改，如需修改直接删除
	private String role;// o团长1队长2副队长3导游4团员
	private String email;//邮箱,非必填
	private String mobile;//手机号
	private String remark;//备注
	private String createdUser;// 创建人用户名
	private String createdDate;// 创建日期
	private String modifiedUser;// 最后修改人用户名
	private String modifiedDate;// 最后修改时间

	public Role(String remark, String name, String role, Integer teamId, String createdUser, String createdDate, String email,
			String mobile) {
		this.role = role;
		this.remark = remark;
		this.name = name;
		this.teamId = teamId;
		this.createdUser = createdUser;
		this.createdDate = createdDate;
		this.email = email;
		this.mobile = mobile;
	}

	public Role() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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
		Map<String, Object> m = new HashMap<>();
		m.put("teamId", teamId);
		m.put("role", role);
		m.put("name", name);
		m.put("remark", remark);
		m.put("id", id);
		m.put("createdUser", createdUser);
		m.put("createdDate", createdDate);
		m.put("modifiedUser", modifiedUser);
		m.put("modifiedDate", modifiedDate);
		m.put("email", email);
		m.put("mobile", mobile);
		return m;
	}

}
