package com.sy.travel.entity;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import com.sy.travel.common.SY;

/**
 * 分类的实体类
 * @author liuxin
 *
 */
@Entity
public class Classes implements SY{
	@Id
	@GeneratedValue
	private Integer id;// 分类id，主键
	private String name;// 分类名字
	private Integer sortId;// 分类序号 
	private Integer parentId;// 父类id
	private String remark;// 备注
	private String createdUser;// 创建者
	private String createdDate;// 创建时间
	private String modifiedUser;// 修改者
	private String modifiedDate;//修改时间
	
	public Classes() {
	}
	
	public Classes(String name, Integer sortId, Integer parentId, String remark) {
		this.name = name;
		this.sortId = sortId;
		this.parentId = parentId;
		this.remark = remark;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSortId() {
		return sortId;
	}
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", id);
		m.put("name", name);
		m.put("sortId", sortId);
		m.put("parentId", parentId);
		m.put("remark", remark);
		m.put("createdUser", createdUser);
		m.put("createdDate", createdDate);
		m.put("modifiedUser", modifiedUser);
		m.put("modifiedDate", modifiedDate);
		return m;
	}
}
