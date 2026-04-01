package com.sy.travel.dto.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductCreateRequest {
	@NotBlank(message = "operator不能为空")
	private String operator;
	@NotBlank(message = "code不能为空")
	private String code;
	@NotBlank(message = "name不能为空")
	private String name;
	@NotBlank(message = "teamId不能为空")
	private String teamId;
	private String exText;
	@NotBlank(message = "onlineDate不能为空")
	private String onlineDate;
	@NotBlank(message = "offlineDate不能为空")
	private String offlineDate;
	@NotBlank(message = "classId不能为空")
	private String classId;
	@NotNull(message = "quantity不能为空")
	private Integer quantity;
	@NotNull(message = "minQty不能为空")
	private Integer minQty;
	@NotNull(message = "soldQty不能为空")
	private Integer soldQty;
	@NotNull(message = "price不能为空")
	private Integer price;
	@NotNull(message = "nights不能为空")
	private Integer nights;
	@NotBlank(message = "status不能为空")
	private String status;
	private String remark;

	public String getOperator() { return operator; }
	public void setOperator(String operator) { this.operator = operator; }
	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getTeamId() { return teamId; }
	public void setTeamId(String teamId) { this.teamId = teamId; }
	public String getExText() { return exText; }
	public void setExText(String exText) { this.exText = exText; }
	public String getOnlineDate() { return onlineDate; }
	public void setOnlineDate(String onlineDate) { this.onlineDate = onlineDate; }
	public String getOfflineDate() { return offlineDate; }
	public void setOfflineDate(String offlineDate) { this.offlineDate = offlineDate; }
	public String getClassId() { return classId; }
	public void setClassId(String classId) { this.classId = classId; }
	public Integer getQuantity() { return quantity; }
	public void setQuantity(Integer quantity) { this.quantity = quantity; }
	public Integer getMinQty() { return minQty; }
	public void setMinQty(Integer minQty) { this.minQty = minQty; }
	public Integer getSoldQty() { return soldQty; }
	public void setSoldQty(Integer soldQty) { this.soldQty = soldQty; }
	public Integer getPrice() { return price; }
	public void setPrice(Integer price) { this.price = price; }
	public Integer getNights() { return nights; }
	public void setNights(Integer nights) { this.nights = nights; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
}
