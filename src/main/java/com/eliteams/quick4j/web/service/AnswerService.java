package com.eliteams.quick4j.web.service;

import java.util.List;
import java.util.Map;

import com.eliteams.quick4j.core.generic.GenericService;
import com.eliteams.quick4j.web.model.Answer;
import com.eliteams.quick4j.web.model.Exam;

/**
 * 答题 业务 接口
 **/
public interface AnswerService extends GenericService<Answer, Long> {
	
	 public int updateScore(Answer model) ;
		

}
