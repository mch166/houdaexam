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
	
	
	
}
