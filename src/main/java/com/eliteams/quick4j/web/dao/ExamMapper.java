package com.eliteams.quick4j.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.eliteams.quick4j.core.feature.orm.mybatis.Page;
import com.eliteams.quick4j.core.generic.GenericDao;
import com.eliteams.quick4j.web.model.Exam;
import com.eliteams.quick4j.web.model.User;
import com.eliteams.quick4j.web.model.UserExample;

/**
 * 试卷Dao接口
 **/
public interface ExamMapper extends GenericDao<Exam, Long> {

}