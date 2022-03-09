package com.qian.controller.manager;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {
    private Logger logger = LoggerFactory.getLogger(BaseController.class);

    public JSONObject resJson(Integer code, String msg, Object obj){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        json.put("obj", obj);

        logger.info(json.toJSONString());

        return json;
    }
}
