package com.qian.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/*
redis操作工具类
 */
@Component
public class RedisUtil {
    @Autowired
    private JedisPool jedisPool;

    private Jedis jedis = null;

    public void connection(){
        if(jedis == null) {
            //建立redis连接
            jedis = jedisPool.getResource();
        }
    }

    /**
     * 数据类型String
     * 向Redis中添加key，并设置有效期
     */
    public Integer set(String key, String value) {
        connection();
        try {
            //添加键指对,如果key已经存在，覆盖原来的值，成功返回OK
            String res = jedis.set(key, value);

            //只有当key的值不存在时，设置key的值,成功返回1 失败返回0
//            long res = jedis.setnx(key, value);

            //设置过期时间，单位秒（s）
            if ("ok".equals(res.toLowerCase())) jedis.expire(key, 5*60);
            return 1;
        } catch (Exception e) {
            jedis.close();
            return 0;
        }
    }

    /**
     * 数据类型String
     * 根据传入Key获取指定Value
     */
    public String get(String key) {
        connection();
        String value = null;
        try {
            value = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
            return value;
        }
    }
}
