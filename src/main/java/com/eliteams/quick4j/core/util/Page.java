package com.eliteams.quick4j.core.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Page<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int page;//当前页码
	
	private int limit;//页面数据数量

	private int code = 0;//0以外的状态均为失败
	
	private String msg;//数据获取失败信息
	
	private int count = 0;//数据总条数
	
	private List<T> data = Collections.emptyList();//当前页数据

	
	
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	/**
	 * 获取页面数据开始
	 * @return
	 */
	public int getStart() {
		int start = (page-1)*limit;
		return start;
	}
	/**
	 * 获取页面数据结束
	 * @return
	 */
	public int getEnd(){
		int end = page*limit;
		return end;
	}

}
