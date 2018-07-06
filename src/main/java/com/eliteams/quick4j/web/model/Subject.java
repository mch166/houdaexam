package com.eliteams.quick4j.web.model;

import java.io.Serializable;

public class Subject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long sjid;
	private String tmtitle;
	private String optionA;
	private String optionB;
	private String optionC;
	private String optionD;
	private String answer;
	private String tmxh;
	private String parse;
	private String type;
	
	private String sjmc;
	
	/**
	 * 单选
	 */
	public static final String TYPE_DANXUAN="1";
	
	/**
	 * 多选
	 */
	public static final String TYPE_DUOXUAN="2";
	
	/**
	 * 不定项
	 */
	public static final String TYPE_BUDINGXIANG="3";
	
	/**
	 * 单选 分值
	 */
	public static final int VALUE_DANXUAN=1;
	
	/**
	 * 多选 分值
	 */
	public static final int VALUE_DUOXUAN=2;
	
	/**
	 * 不定项 分值
	 */
	public static final int VALUE_BUDINGXIANG=2;
	
	
	
	
	public Long getSjid() {
		return sjid;
	}
	public void setSjid(Long sjid) {
		this.sjid = sjid;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTmtitle() {
		return tmtitle;
	}
	public void setTmtitle(String tmtitle) {
		this.tmtitle = tmtitle;
	}
	public String getOptionA() {
		return optionA;
	}
	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}
	public String getOptionB() {
		return optionB;
	}
	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}
	public String getOptionC() {
		return optionC;
	}
	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}
	public String getOptionD() {
		return optionD;
	}
	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getTmxh() {
		return tmxh;
	}
	public void setTmxh(String tmxh) {
		this.tmxh = tmxh;
	}
	public String getParse() {
		return parse;
	}
	public void setParse(String parse) {
		this.parse = parse;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSjmc() {
		return sjmc;
	}
	public void setSjmc(String sjmc) {
		this.sjmc = sjmc;
	}
	
	
	
	
	
}
