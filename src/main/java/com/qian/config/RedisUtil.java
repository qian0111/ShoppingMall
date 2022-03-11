package com.qian.config;

import com.qian.model.manager.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public Integer set(List<Goods> goodsList) {
        connection();
        try {
            //添加键指对,如果key已经存在，覆盖原来的值，成功返回OK
            Map<String, String> value = new HashMap<>();
            int i=0;
            for (Goods goods:goodsList){
                value.put("id", goods.getId().toString());
                value.put("gName", goods.getgName());
                value.put("gImage", goods.getgImage());
                value.put("gPrice", goods.getgPrice().toString());
                value.put("gCount", goods.getgCount().toString());
                String res = jedis.hmset("rec"+(i++), value);
                if ("ok".equals(res.toLowerCase())) jedis.expire("rec"+goodsList.indexOf(goods), 5*60);
            }
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

    public List<Goods> getList() {
        connection();
        List<Goods> goodsList = new ArrayList<>();
        try {
            for(int i=0; i<6; i++){
                Goods value = new Goods();
                value.setId(Integer.valueOf(jedis.hget("rec"+i, "id")));
                value.setgName(jedis.hget("rec"+i,"gName"));
                value.setgImage(jedis.hget("rec"+i,"gImage"));
                BigDecimal gPrice = new BigDecimal(jedis.hget("rec"+i,"gPrice"));
                value.setgPrice(gPrice);
                value.setgCount(Integer.valueOf(jedis.hget("rec"+i, "gCount")));
                goodsList.add(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
            return goodsList;
        }
    }
}
