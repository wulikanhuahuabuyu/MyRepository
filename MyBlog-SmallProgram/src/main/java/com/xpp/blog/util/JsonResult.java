package com.xpp.blog.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/*
 * 封装服务器给浏览器的响应数据
 * 
 * @JsonInclude(value=Include.NON_NULL)
 * 可以返回指定格式的json数据,如果属性返回值为空，则不返回任何内容
 * @JsonInclude(value=Include.ALWAYS)
 * 默认使用，返回任何数据（包括null）
 */
public class JsonResult<T> {
	
	@JsonInclude(value=Include.ALWAYS)
	private Integer state;
	
	@JsonInclude(value=Include.NON_NULL)
	private String message;
	
	@JsonInclude(value=Include.ALWAYS)
	private T data;

	public JsonResult() {
	}

	public JsonResult(Integer state) {
		this.state = state;
	}
	
	public JsonResult(Integer state, String message) {
		super();
		this.state = state;
		this.message = message;
	}

	public JsonResult(Integer state, T data) {
		super();
		this.state = state;
		this.data = data;
	}
	
	public JsonResult(T data) {
		super();
		this.data = data;
	}

	public JsonResult(String message) {
		this.message = message;
	}

	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

}

