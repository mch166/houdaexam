package com.eliteams.quick4j.core.util;

import java.util.Map;


/**
 * $.ajax后需要接受的JSON
 * 
 * @author HL
 * 
 */
public class AjaxJson {

	private boolean success = true;// 是否成功
	private String msg = "操作成功";// 提示信息
	private Object obj = null;// 其他信息
	private Map<String, Object> attributes;// 其他参数
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public static AjaxJson errorAjax(Exception ex) {
		AjaxJson aj = new AjaxJson();
		aj.setSuccess(false);
		aj.setMsg("<font color='red'>请求数据出现错误!</font>原因:" + ex.getMessage());
		return aj;
	}

	public static AjaxJson errorAjax(String msg) {
		AjaxJson aj = new AjaxJson();
		aj.setSuccess(false);
		aj.setMsg(msg);
		return aj;
	}

	public static AjaxJson okAjax(String msg) {
		AjaxJson aj = new AjaxJson();
		aj.setSuccess(true);
		aj.setMsg(msg);
		return aj;
	}

	public static AjaxJson okAjax() {
		return okAjax("操作成功！");
	}
}
