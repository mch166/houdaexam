package com.eliteams.quick4j.web.service;

import java.util.List;
import java.util.Map;

import com.eliteams.quick4j.core.generic.GenericService;
import com.eliteams.quick4j.web.model.Subject;
import com.eliteams.quick4j.web.model.User;

/**
 * 题目 业务 接口
 * 
 **/
public interface SubjectService extends GenericService<Subject, Long> {

	Map<String, Object> selectBySjid(Map map);
}
