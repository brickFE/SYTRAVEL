package com.sy.travel.dto.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductUpdateRequest {
	@NotBlank(message = "operator不能为空")
	private String operator;
	@NotNull(message = "id不能为空")
	private Integer id;
	private String name;
	private String exText;
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
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getExText() { return exText; }
	public void setExText(String exText) { this.exText = exText; }
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
