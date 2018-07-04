package com.eliteams.quick4j.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.eliteams.quick4j.core.generic.GenericDao;
import com.eliteams.quick4j.core.generic.GenericServiceImpl;
import com.eliteams.quick4j.web.dao.AnswerMapper;
import com.eliteams.quick4j.web.dao.ExamMapper;
import com.eliteams.quick4j.web.dao.UserMapper;
import com.eliteams.quick4j.web.model.Answer;
import com.eliteams.quick4j.web.model.Exam;
import com.eliteams.quick4j.web.model.Subject;
import com.eliteams.quick4j.web.model.User;
import com.eliteams.quick4j.web.model.UserExample;
import com.eliteams.quick4j.web.service.AnswerService;
import com.eliteams.quick4j.web.service.ExamService;
import com.eliteams.quick4j.web.service.UserService;

/**
 * 答题实现类
 */
@Service
public class AnswerServiceImpl extends GenericServiceImpl<Answer, Long> implements AnswerService {

	@Resource
	private AnswerMapper answerMapper;
	
	@Override
	public GenericDao<Answer, Long> getDao() {
		// TODO Auto-generated method stub
		return answerMapper;
	}

	@Override
	public Map selectList(Map paramMap) {
		Map<String, Object> retMap = new HashMap<String, Object>();
        List<Answer> result = answerMapper.selectByOther(paramMap);
    	int rowCnts = answerMapper.getRowCnts();
        retMap.put("total", rowCnts);
        retMap.put("list", result);
        return retMap;
	}

	@Override
	public Answer selectById(Long id) {
		// TODO Auto-generated method stub
		return answerMapper.selectByPrimaryKey(id);
	}
	
	
	@Override
	public int insert(Answer answer) {
		// TODO Auto-generated method stub
		return answerMapper.insert(answer);
	}
   
	
	  @Override
	    public int updateScore(Answer answer) {
	        return answerMapper.updateScore(answer);
	    }

	    @Override
	    public int delete(Long id) {
	        return answerMapper.deleteByPrimaryKey(id);
	    }


}
