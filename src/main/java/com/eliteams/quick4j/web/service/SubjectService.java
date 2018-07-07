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
	
    /**
     * 根据试卷和题目序号查询
     * @param sjid
     * @param tmxh
     * @return
     */
    Subject selectByTmxh(Long sjid,String tmxh);
    
    /**
     * 根据试卷id删除试题
     * @param sjid
     * @return
     */
    int deleteBySjid(String sjid);
    
    Map<String, Object> selectAll(Map map);
}
