package com.qian.job;

import com.qian.controller.manager.GoodsController;
import com.qian.service.manager.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/*
定时任务
 */
@Configuration    //1. 主要用于标记配置类，兼备@Component的效果。
//@EnableScheduling //2. 开启定时任务
public class MyJob {
    private Logger logger = LoggerFactory.getLogger(GoodsController.class);
    @Autowired
    private IOrderService orderService;
    /*
    Cron表达式参数分别表示：* * * * * *
    秒（0~59） 例如0/5表示每5秒
    分（0~59）
    时（0~23）
    日（0~31）的某天，需计算
    月（0~11）
    周几（ 可填1-7 或 SUN/MON/TUE/WED/THU/FRI/SAT）

    示例：
    12 * * * * *   每一分钟的第12秒执行 11 12
    0/5 * * * * *  从0秒开始的，每5秒执行一次 11 15
    * /5 从任务启动开始，每5秒执行一次  11 16

    0 0 0/1 * * * 从0点开始，每隔一个小时执行一次
    0 0 13 * * * 每一天的下午1点执行
    0 0 6 1 * *  每个月的1号的早上6点执行
     */
    @Scheduled(cron = "0 0 20 * * *")
    public void task(){
        int row = orderService.deliverTask();
        logger.info("定时发货 状态码："+ row);
    }
}
