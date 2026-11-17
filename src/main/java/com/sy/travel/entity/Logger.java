package com.sy.travel.entity;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import com.sy.travel.common.SY;

/**
 * 日志信息的实体类
 * 
 * @author liuxin
 *
 */
@Entity
public class Logger implements SY {
	@Id
	@GeneratedValue
	private Integer id;// 日志id
	private String operator;// 操作者
	private String start;// 开始时间
	private String end;// 结束时间
	private String remark;// 备注
	private String status;// 操作状态：0失败，1成功
	private String operation;// 进行的操作

	public Logger() {
	}

	/**
	 * 日志信息的构造函数
	 * @param operator	操作者
	 * @param start		开始时间
	 * @param end		结束时间
	 * @param remark	备注：保存的是操作结果信息
	 * @param status	记录操作的结果，"0"：失败；"1"：成功
	 * @param operation
	 */
	public Logger(String operator, String start, String end, String remark, String status, String operation) {
		this.operator = operator;
		this.start = start;
		this.end = end;
		this.remark = remark;
		this.status = status;
		this.operation = operation;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return toMap().toString();
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<>();
		m.put("id", id);
		m.put("operator", operator == null ? "" : operator);
		m.put("start", start);
		m.put("end", end);
		m.put("remark", remark);
		m.put("status", status);
		m.put("operation", operation);
		return m;
	}

}
