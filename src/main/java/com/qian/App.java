package com.qian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application
 * springboot项目的启动类
 *
 * 注意：启动类要在其他类的上一级目录
 */
@SpringBootApplication
@MapperScan("com.qian.dao") //扫描dao层，并创建dao的实现类
public class App {
    /*
    @SpringBootApplication是一个组合注解，它整合了
    @SpringBootConfiguration：标记当前类为配置类，相关配置会在启动时加载
    @EnableAutoConfiguration：开启自动配置
    @ComponentScan：扫描主类所在的同级包以及下级包里的Bean
     */
    public static void main( String[] args ) {
        SpringApplication.run(App.class);
    }
}
