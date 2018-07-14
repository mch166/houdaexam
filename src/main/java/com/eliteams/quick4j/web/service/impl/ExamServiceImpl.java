package com.eliteams.quick4j.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.eliteams.quick4j.core.generic.GenericDao;
import com.eliteams.quick4j.core.generic.GenericServiceImpl;
import com.eliteams.quick4j.web.dao.ExamMapper;
import com.eliteams.quick4j.web.dao.UserMapper;
import com.eliteams.quick4j.web.model.Exam;
import com.eliteams.quick4j.web.model.Subject;
import com.eliteams.quick4j.web.model.User;
import com.eliteams.quick4j.web.model.UserExample;
import com.eliteams.quick4j.web.service.ExamService;
import com.eliteams.quick4j.web.service.UserService;

/**
 * 试卷实现类
 */
@Service
public class ExamServiceImpl extends GenericServiceImpl<Exam, Long> implements ExamService {

	@Resource
	private ExamMapper examMapper;
	
	@Override
	public GenericDao<Exam, Long> getDao() {
		// TODO Auto-generated method stub
		return examMapper;
	}

	@Override
	public Map selectList(Map paramMap) throws Exception{
		Map<String, Object> retMap = new HashMap<String, Object>();
        List<Exam> result = examMapper.selectByExample(paramMap);
    	int rowCnts = examMapper.getRowCnts();
        retMap.put("total", rowCnts);
        retMap.put("list", result);
        return retMap;
	}

	@Override
	public Exam selectById(Long id) {
		// TODO Auto-generated method stub
		return examMapper.selectByPrimaryKey(id);
	}
	
	
	@Override
	public Long insertExam(Exam exam) {
		// TODO Auto-generated method stub
		return examMapper.insertExam(exam);
	}
   
	
	  @Override
	    public int update(Exam model) {
	        return examMapper.updateByPrimaryKeySelective(model);
	    }

	    @Override
	    public int delete(Long id) {
	        return examMapper.deleteByPrimaryKey(id);
	    }

		@Override
		public void setExamSfky(int id) {
			examMapper.setAllExamSfkyIsNo();
			examMapper.setExamSfkyIsYes(id);
			
		}


}
