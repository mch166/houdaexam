package com.eliteams.quick4j.web.model;

import java.io.Serializable;

public class Exam implements Serializable{
	/**
	 * 试卷
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String code;
	private String sfky = "否";//是否可用
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSfky() {
		return sfky;
	}
	public void setSfky(String sfky) {
		this.sfky = sfky;
	}
	
	
	
	
	
}
