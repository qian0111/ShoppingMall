package com.qian.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/*
redis：非关系型数据库（NoSQL）
特性：
1 基于内存的，mysql的数据存储在硬盘中
2 可选持久性的键值对(Key-Value)存储数据库，即可以把数据持久化倒硬盘，有两方式：RDB与AOF
3 redis是单线程的

五大数据类型
1 string 字符串

2 hash 散列
Redis hash 是一个 string 类型的 field 和 value 的映射表，
hash 特别适合用于存储对象,类似于java里面的Map<String, Object>

3 list 列表
Redis 列表是简单的字符串列表，按照插入顺序排序。
你可以添加一个元素到列表的头部（左边）或者尾部（右边）。
他的底层实现是个链表，链表可以两端双向插入。

4 set 集合
Redis的Set是string类型的无序集合。
集合是通过哈希表（HashTable）实现的，所以添加，删除，查找的复杂度都是O(1)。

5 Zset（sorted set 有序集合）：
Redis zset 和 set 一样也是string类型元素的集合,且不允许重复的成员。
不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。
zset的成员是唯一的,但分数(score)却可以重复。
 */

//声明是配置类，且兼具@Component的作用
@Configuration
public class RedisConfig {
    @Value("${redis.host}") //读取yml配置文件，将配置文件中的值，赋值值变量
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.timeout}")
    private int timeout;

    @Value("${redis.pool.max-idle}")
    private int maxIdle;

    @Value("${redis.pool.max-wait}")
    private int maxWaitMillis;

    @Value("${redis.blockWhenExhausted}")
    private Boolean blockWhenExhausted;

    @Value("${redis.password}")
    private String passWord;

    @Value("${redis.database}")
    private int dataBase;

    @Bean //将本方法返回的类对象，注入到ioc容器中去
    public JedisPool jedisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //加载连接池配置
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 连接耗尽时是否阻塞, false报异常,true阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);

        // 是否启用pool的jmx监控，可用于监控资源使用状态 【默认值：true】
        //使用建议：开启
//        jedisPoolConfig.setJmxEnabled(JmxEnabled);
        //创建redis连接类
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, null, dataBase);

        return jedisPool;
    }

}
