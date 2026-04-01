package com.sy.travel.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.sy.travel.common.SY;
import com.sy.travel.common.PasswordSupport;

/**
 * 用户信息
 * @author liuxin
 *
 */
@Entity
public class User implements SY{
	@Id
	@GeneratedValue
	private Integer id;//自增长ID也是 用户的ID，用来做主键使用
	private String username;// 用户名，不可修改用户名，如需直接删除重新创建即可
	private String password;// 密码
	private String remark;// 备注，描述
	private String permission;//权限：0管理员，1项目负责人，2团队负责人，3产品经理，4审计，5监控
	
	public User() {
	}

	public User(String username, String password, String remark, String permission) {
		this.username = username;
		this.password = password;
		this.remark = remark;
		this.permission = permission;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		if (PasswordSupport.isBcryptHash(this.password)) {
			return this.password;
		}
		return PasswordSupport.legacyDecode(this.password);
	}

	public String getEncodedPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		return toMap().toString();
	}
	
	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<>();
		m.put("username", username);
		m.put("password", password);
		m.put("remark", remark);
		m.put("permission", permission);
		m.put("id", id);
		return m;
	}
}
