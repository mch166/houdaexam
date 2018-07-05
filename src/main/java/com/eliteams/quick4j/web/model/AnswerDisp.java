package com.eliteams.quick4j.web.model;

import java.io.Serializable;
import java.util.Map;
/**
 * 答题展示实体
 * @author mengchong
 *
 */
public class AnswerDisp implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String userid;
	private String sjid;
	private Map answerMap;
	private String answerTime;//答题所用时间
	private String score;
	private String submitTime;//交卷时间
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
	
	public Map getAnswerMap() {
		return answerMap;
	}
	public void setAnswerMap(Map answerMap) {
		this.answerMap = answerMap;
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
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
        return "Answer [id=" + id + ", userid=" + userid + ", sjid=" + sjid + ", answerTime=" + answerTime + ", score=" + score + ", submitTime=" + submitTime + ", answer=" + answerMap + "]";
	}
	
}
