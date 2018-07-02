package com.eliteams.quick4j.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.eliteams.quick4j.core.generic.GenericDao;
import com.eliteams.quick4j.core.generic.GenericServiceImpl;
import com.eliteams.quick4j.web.dao.UserMapper;
import com.eliteams.quick4j.web.model.Exam;
import com.eliteams.quick4j.web.model.User;
import com.eliteams.quick4j.web.model.UserExample;
import com.eliteams.quick4j.web.service.ExamService;
import com.eliteams.quick4j.web.service.UserService;

/**
 * 试卷实现类
 */
@Service
public class ExamServiceImpl extends GenericServiceImpl<Exam, Long> implements ExamService {

	@Override
	public GenericDao<Exam, Long> getDao() {
		// TODO Auto-generated method stub
		return null;
	}

   

}
