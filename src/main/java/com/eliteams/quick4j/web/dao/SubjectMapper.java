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
 * 试题Dao接口
 **/
public interface SubjectMapper extends GenericDao<Subject, Long> {
	
   int insert(Subject subject);
	
	List selectBySjid(Map map);
	
	List selectAll(Map map);
	
    /**
     * 获得记录数
     * @return
     */
    int getRowCnts();
    
    /**
     * 根据试卷和题目序号查询
     * @param sjid
     * @param tmxh
     * @return
     */
    Subject selectByTmxh(@Param("sjid") Long sjid,@Param("tmxh")String tmxh);
    
    /**
     * 根据试卷id删除试题
     * @param sjid
     * @return
     */
    int deleteBySjid(String sjid);
    
}