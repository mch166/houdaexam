package com.eliteams.quick4j.web.dao;

import java.util.List;
import java.util.Map;

import com.eliteams.quick4j.core.generic.GenericDao;
import com.eliteams.quick4j.web.model.Exam;

/**
 * 试卷Dao接口
 **/
public interface ExamMapper extends GenericDao<Exam, Long> {

	/**
	 * 试题列表
	 */
	
		List selectByExample(Map map);
		
		/**
		 * 根据主键获取试题
		 * 
		 */
		
		Exam selectByPrimaryKey(int id);
		
		/**
		 * 插入
		 */
		Long insertExam(Exam exam);
		
		  /**
	     * 获得记录数
	     * @return
	     */
	    int getRowCnts();
	
}