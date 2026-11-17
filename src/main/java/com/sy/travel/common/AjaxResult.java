package com.sy.travel.common;

/**
 * Ajax请求（HTTP请求）响应的格式（最外层对象）
 * 
 * @author liuxin
 *
 */
public class AjaxResult<T> {
	// 返回的状态码
	private int code;
	// 返回的状态信息
	private String msg;
	// 返回的结果对象:具体的内容
	private T data;

	public AjaxResult(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static <T> AjaxResult<T> success(T data) {
		return new AjaxResult<T>(200, ApiMessages.SUCCESS, data);
	}

	public static <T> AjaxResult<T> failed(int code, T data) {
		return new AjaxResult<T>(code, ApiMessages.FAILED, data);
	}

	public static AjaxResult<String> validationError(String message) {
		return new AjaxResult<String>(400, ErrorCodes.VALIDATION_ERROR, message);
	}

	public static AjaxResult<String> internalError(String message) {
		return new AjaxResult<String>(500, ErrorCodes.INTERNAL_ERROR, message);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
