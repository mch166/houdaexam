package com.eliteams.quick4j.web.interceptors;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import com.eliteams.quick4j.core.util.RedisUtils;
import com.eliteams.quick4j.web.model.User;

import redis.clients.jedis.Jedis;
 
public class SessionListener1 implements SessionListener {
 
	@Override
	public void onStart(Session session) {//会话创建触发 已进入shiro的过滤连就触发这个方法
		// TODO Auto-generated method stub
		System.out.println("会话创建：" + session.getId());
	}
 
	@Override
	public void onStop(Session session) {//退出
		User user = (User) session.getAttribute("userInfo");
		if(user!=null&&user.getType()!=1) {
		        Jedis jedis = RedisUtils.getJedis();
		        jedis.setex("user_"+user.getUsername(), 30*60, "user_"+user.getUsername());
		        RedisUtils.returnResource(jedis);
		}
	}
 
	@Override
	public void onExpiration(Session session) {//会话过期时触发
		User user = (User) session.getAttribute("userInfo");
		System.out.println(user);
		if(user!=null&&user.getType()!=1) {
		        Jedis jedis = RedisUtils.getJedis();
		        jedis.setex("user_"+user.getUsername(), 30*60, "user_"+user.getUsername());
		        RedisUtils.returnResource(jedis);
		}

	}
 
}
