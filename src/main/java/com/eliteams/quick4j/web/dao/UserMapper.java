package com.eliteams.quick4j.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.eliteams.quick4j.core.feature.orm.mybatis.Page;
import com.eliteams.quick4j.core.generic.GenericDao;
import com.eliteams.quick4j.web.model.User;
import com.eliteams.quick4j.web.model.UserExample;

/**
 * 用户Dao接口
 * 
 * @author StarZou
 * @since 2014年7月5日 上午11:49:57
 **/
public interface UserMapper extends GenericDao<User, Long> {
    int countByExample(UserExample example);

    int deleteByPrimaryKey(Long id);


    int insertSelective(User record);

    List<User> selectByExample(Map paramMap);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);
    
    int updatePwd(User record);



    /**
     * 用户登录验证查询
     * 
     * @param record
     * @return
     */
    User authentication(@Param("record") User record);

   
    /**
     * 获得记录数
     * @return
     */
    int getRowCnts();
}