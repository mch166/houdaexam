package com.eliteams.quick4j.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.eliteams.quick4j.core.feature.orm.mybatis.Page;
import com.eliteams.quick4j.core.generic.GenericDao;
import com.eliteams.quick4j.web.model.Subject;
import com.eliteams.quick4j.web.model.User;
import com.eliteams.quick4j.web.model.UserExample;

/**
 * 用户Dao接口
 * 
 * @author StarZou
 * @since 2014年7月5日 上午11:49:57
 **/
public interface SubjectMapper extends GenericDao<Subject, Long> {
	
   int insertSelective(Subject subject);
	
	List selectBySjid(Map map);
	
    /**
     * 获得记录数
     * @return
     */
    int getRowCnts();
    
}