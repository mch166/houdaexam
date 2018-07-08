package com.eliteams.quick4j.web.model;

import java.io.Serializable;

public class Answer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String userid;
	private String sjid;
	private String answer;
	private String answerTime;//答题所用时间
	private String score;
	private String submitTime;//交卷时间
	
	private String username;
	private String sjName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid) {
		this.sjid = sjid;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswerTime() {
		return answerTime;
	}
	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSjName() {
		return sjName;
	}
	public void setSjName(String sjName) {
		this.sjName = sjName;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
        return "Answer [id=" + id + ", userid=" + userid + ", sjid=" + sjid + ", answerTime=" + answerTime + ", score=" + score + ", submitTime=" + submitTime + ", answer=" + answer + "]";
	}
	
}
