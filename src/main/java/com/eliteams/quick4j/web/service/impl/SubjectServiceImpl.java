package com.eliteams.quick4j.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.eliteams.quick4j.core.generic.GenericDao;
import com.eliteams.quick4j.core.generic.GenericServiceImpl;
import com.eliteams.quick4j.core.util.RedisUtil;
import com.eliteams.quick4j.core.util.SerializeUtil;
import com.eliteams.quick4j.web.dao.SubjectMapper;
import com.eliteams.quick4j.web.dao.UserMapper;
import com.eliteams.quick4j.web.model.Subject;
import com.eliteams.quick4j.web.model.User;
import com.eliteams.quick4j.web.model.UserExample;
import com.eliteams.quick4j.web.service.SubjectService;
import com.eliteams.quick4j.web.service.UserService;

import redis.clients.jedis.Jedis;

/**
 * 用户Service实现类
 *
 * @author StarZou
 * @since 2014年7月5日 上午11:54:24
 */
@Service
public class SubjectServiceImpl extends GenericServiceImpl<Subject, Long> implements SubjectService {

    @Resource
    private SubjectMapper subjectMapper;

    @Override
    public int insert(Subject subject) {
        return subjectMapper.insert(subject);
    }


    @Override
    public GenericDao<Subject, Long> getDao() {
        return subjectMapper;
    }


	@Override
	public Map<String, Object> selectBySjid(Map map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
        List<User> result = subjectMapper.selectBySjid(map);
    	int rowCnts = subjectMapper.getRowCnts();
        retMap.put("total", rowCnts);
        retMap.put("list", result);
        return retMap;
	}
	
	@Override
	public Map<String, Object> selectAll(Map map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
        List<User> result = subjectMapper.selectAll(map);
    	int rowCnts = subjectMapper.getRowCnts();
        retMap.put("total", rowCnts);
        retMap.put("list", result);
        return retMap;
	}
	

	  @Override
	    public Subject selectById(Long id) {
	        return subjectMapper.selectByPrimaryKey(id);
	    } 
	  
	  @Override
	    public Subject selectByTmxh(Long sjid,String tmxh) {
		  //先从缓存中取
		  Subject subject=null;
		  try {
			  RedisUtil redisUtil = new RedisUtil();
		        Jedis jedis = redisUtil.getJedis();
		        byte[] key=("TM_"+sjid+"_"+tmxh).getBytes();
		        Boolean exists = jedis.exists(key);
		        if(exists) {
			        byte[] data = jedis.get(key);
			        if(data!=null) {
			        	 subject=(Subject)SerializeUtil.unserialize(data);
			        }
		        }
		        if(subject==null) {
		        	subject=subjectMapper.selectByTmxh(sjid,tmxh);
			        jedis.setex(key, 180*60, SerializeUtil.serialize(subject));
		        }
		  }catch (Exception e) {
			  e.printStackTrace();
			  //"根据试卷和序号获取试题失败"
		}
	        return subject;
	 } 
	  
	  @Override
	    public int update(Subject model) {
	        return subjectMapper.updateByPrimaryKeySelective(model);
	    }

	    @Override
	    public int delete(Long id) {
	        return subjectMapper.deleteByPrimaryKey(id);
	    }
	    
	    /**
	     * 根据试卷id删除试题
	     * @param sjid
	     * @return
	     */
	    @Override
	   public int deleteBySjid(String sjid) {
	    	return subjectMapper.deleteBySjid(sjid);
	    }

	    public static void main(String[] args) {
	    	RedisUtil redisUtil = new RedisUtil();
	        Jedis jedis = redisUtil.getJedis();
	        byte[] key=("TM_"+0+"_"+1).getBytes();
	        Boolean exists = jedis.exists(key);
			  Subject subject=null;
	        if(exists) {
		        byte[] data = jedis.get(key);
		        if(data!=null) {
		        	 subject=(Subject)SerializeUtil.unserialize(data);
		        }
	        }
	        if(subject==null) {
	        	subject=new Subject();
	        	subject.setId(1L);
	        	subject.setOptionA("2222");
		        jedis.setex(key, 180*60, SerializeUtil.serialize(subject));
	        }
	        System.out.println(subject.getOptionA());
	        
	       jedis.setex("user_mch", 30*60, "user_"+"mch");
	        System.out.println(jedis.get("user_mch"));
	        Long del = jedis.del("user_mch");
	        System.out.println("del==="+del);

		}
}
