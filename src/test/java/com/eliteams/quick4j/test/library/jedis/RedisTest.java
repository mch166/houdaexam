package com.eliteams.quick4j.test.library.jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.eliteams.quick4j.core.util.RedisUtils;

import redis.clients.jedis.Jedis;

public class RedisTest {


    // 字符串操作
    @Test
    public void testStr() {
        Jedis jedis = RedisUtils.getJedis();
        jedis.setex("idTest", 60, "11111");
        RedisUtils.returnResource(jedis);
    }
    
    
    // 字符串操作
    @Test
    public void testGetStr() {
        Jedis jedis = RedisUtils.getJedis();
        Boolean exists = jedis.exists("idTest");
        System.out.println(exists);
        
        String id = jedis.get("idTest");
        System.out.println(id);
        RedisUtils.returnResource(jedis);
    }
    // 操作 map
    @Test
    public void testMap() {
        Jedis jedis = RedisUtils.getJedis();
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "xinxin");
        map.put("age", "22");
        map.put("qq", "123456");
        jedis.hmset("user", map);
        List<String> rsmap = jedis.hmget("user", "name", "age", "qq");
        System.out.println(rsmap);
        jedis.hdel("user", "age");
        Iterator<String> iter = jedis.hkeys("user").iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + jedis.hmget("user", key));
        }
        RedisUtils.returnResource(jedis);
    }
    
    // 操作 list
    @Test
    public void testList() {
        Jedis jedis = RedisUtils.getJedis();
        jedis.del("java framework");
        System.out.println(jedis.lrange("java framework", 0, -1));
        jedis.lpush("java framework", "spring");
        jedis.lpush("java framework", "struts");
        jedis.lpush("java framework", "hibernate");
        System.out.println(jedis.lrange("java framework", 0, -1));
        jedis.del("java framework");
        jedis.rpush("java framework", "spring");
        jedis.rpush("java framework", "struts");
        jedis.rpush("java framework", "hibernate");
        System.out.println(jedis.lrange("java framework", 0, -1));
        RedisUtils.returnResource(jedis);
    }

    // 操作 set
    @Test
    public void testSet() {
        Jedis jedis = RedisUtils.getJedis();
        jedis.sadd("user1", "liuling");
        jedis.sadd("user1", "xinxin");
        jedis.sadd("user1", "ling");
        jedis.sadd("user1", "zhangxinxin");
        jedis.sadd("user1", "who");
        jedis.srem("user1", "who"); // 移除noname
        System.out.println(jedis.smembers("user1"));// 获取所有加入的value
        System.out.println(jedis.sismember("user1", "who"));// 判断 who
        System.out.println(jedis.srandmember("user1")); // 是否是user集合的元素
        System.out.println(jedis.scard("user1"));// 返回集合的元素个数
        RedisUtils.returnResource(jedis);
    }

    // jedis 排序
    @Test
    public void testOrder() {
        Jedis jedis = RedisUtils.getJedis();
        jedis.del("a");
        jedis.rpush("a", "1");
        jedis.lpush("a", "6");
        jedis.lpush("a", "3");
        jedis.lpush("a", "9");
        System.out.println(jedis.lrange("a", 0, -1));  
        System.out.println(jedis.sort("a"));          
        System.out.println(jedis.lrange("a", 0, -1));
        RedisUtils.returnResource(jedis);
    }

}