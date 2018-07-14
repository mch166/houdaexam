package com.eliteams.quick4j.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.eliteams.quick4j.core.feature.orm.mybatis.Page;
import com.eliteams.quick4j.core.generic.GenericDao;
import com.eliteams.quick4j.web.model.Answer;
import com.eliteams.quick4j.web.model.Subject;
import com.eliteams.quick4j.web.model.User;
import com.eliteams.quick4j.web.model.UserExample;

/**
 * 答题Dao接口
 * 
 **/
public interface AnswerMapper extends GenericDao<Answer, Long> {
	
   int insert(Answer answer);
	
	List selectByOther(Map map);
	
	int updateAnswer(Answer answer);
	
	int updateScore(Answer answer);
	
    /**
     * 获得记录数
     * @return
     */
    int getRowCnts();
    
   
}