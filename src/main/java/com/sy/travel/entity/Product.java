package com.sy.travel.entity;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import com.sy.travel.common.SY;

@Entity
public class Product implements SY {
	@Id
	@GeneratedValue
	private Integer id;
	private String code;// 产品编号，不能修改
	private String name;// 产品名称
	private Integer teamId;// 团号id，不能修改
	private String exText;// 特殊提示
	private String onlineDate;// 上架时间，不能修改
	private String offlineDate;// 下架时间，不能修改
	private Integer quantity;// 预售数量
	private Integer minQty;// 最低数量
	private Integer soldQty;// 已售数量
	private Integer price;// 产品价格
	private Integer classId;// 产品分类编号id，不能修改
	private Integer nights;// 晚数，几个晚上的游玩时间
	private String status;// 产品状态：0待售，1上架，2下架
	private String remark;// 备注
	private String createdUser;// 创建人用户名
	private String createdDate;// 创建日期
	private String modifiedUser;// 最后修改人用户名
	private String modifiedDate;// 最后修改时间

	public Product() {
		
	}
	
	public Product(String code, String name, Integer teamId, String onlineDate, String offlineDate, Integer quantity,
			Integer minQty, Integer soldQty, Integer price, Integer classId, Integer nights, String status,
			String remark, String createdUser, String createdDate) {
		super();
		this.code = code;
		this.name = name;
		this.teamId = teamId;
		this.onlineDate = onlineDate;
		this.offlineDate = offlineDate;
		this.quantity = quantity;
		this.minQty = minQty;
		this.soldQty = soldQty;
		this.price = price;
		this.classId = classId;
		this.nights = nights;
		this.status = status;
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

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public String getExText() {
		return exText;
	}

	public void setExText(String exText) {
		this.exText = exText;
	}

	public String getOnlineDate() {
		return onlineDate;
	}

	public void setOnlineDate(String onlineDate) {
		this.onlineDate = onlineDate;
	}

	public String getOfflineDate() {
		return offlineDate;
	}

	public void setOfflineDate(String offlineDate) {
		this.offlineDate = offlineDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getMinQty() {
		return minQty;
	}

	public void setMinQty(Integer minQty) {
		this.minQty = minQty;
	}

	public Integer getSoldQty() {
		return soldQty;
	}

	public void setSoldQty(Integer soldQty) {
		this.soldQty = soldQty;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public Integer getNights() {
		return nights;
	}

	public void setNights(Integer nights) {
		this.nights = nights;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		m.put("id", id);
		m.put("code", code);
		m.put("name", name);
		m.put("teamId", teamId);
		m.put("exText", exText);
		m.put("remark", remark);
		m.put("onlineDate", onlineDate);
		m.put("offlineDate", offlineDate);
		m.put("quantity", quantity);
		m.put("minQty", minQty);
		m.put("soldQty", soldQty);
		m.put("price", price);
		m.put("classId", classId);
		m.put("nights", nights);
		m.put("status", status);
		m.put("createdUser", createdUser);
		m.put("createdDate", createdDate);
		m.put("modifiedUser", modifiedUser);
		m.put("modifiedDate", modifiedDate);
		return m;
	}
}
