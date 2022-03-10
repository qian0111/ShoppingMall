package com.qian.controller.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qian.model.manager.Category;
import com.qian.service.manager.ICategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/*
后台-商品类目控制器
 */
@Controller
@RequestMapping("/categorymanage")
public class CategoryController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private ICategoryService categoryService;

    //商品类目页
    @RequestMapping("/categoryPage")
    public ModelAndView categoryPage(){
        //1查询所有分类集合:查询所有一级分类
        List<Category> list = categoryService.queryAll(new Category(0));

        //2 组装layui树形组件，数据格式JSONArray
        JSONArray arr = new JSONArray();
        for (Category cg : list) {
            JSONObject json = new JSONObject();
            json.put("id", cg.getId());
            json.put("title", cg.getName());
            //递归查询
            kidQuery(cg.getId(), json);
            arr.add(json);
        }
        //3 添加数据
        ModelAndView mv = new ModelAndView();
        mv.addObject("data",arr);

        logger.info(arr.toJSONString());

        mv.setViewName("business/category/tree");
        return mv;
    }

    public void kidQuery(Integer parentId, JSONObject json){
        //查询子分类
        List<Category> list = categoryService.queryAll(new Category(parentId));
        if(list == null || list.size() == 0){
            return;
        } else {
            JSONArray arr = new JSONArray();
            for (Category c : list) {
                JSONObject kidJson = new JSONObject();
                kidJson.put("id", c.getId());
                kidJson.put("title", c.getName());
                //递归
                kidQuery(c.getId(), kidJson);

                arr.add(kidJson);
            }
            json.put("children", arr);
        }
    }

    //添加类目
    @RequestMapping("/add")
    @ResponseBody
    public JSONObject add(Category c){
        Integer res = categoryService.add(c);
        if (res == 1){
            //1查询所有分类集合
            List<Category> list = categoryService.queryAll(new Category(0));
            //2 组装layui树形组件，数据格式JSONArray
            JSONArray arr = new JSONArray();
            for (Category cg : list) {
                JSONObject json = new JSONObject();
                json.put("id", cg.getId());
                json.put("title", cg.getName());
                //递归查询
                kidQuery(cg.getId(), json);
                arr.add(json);
            }
            logger.info("添加分类：" + c.getName());
            return resJson(1, "add success", arr);
        } else {
            return resJson(0, "add fail", null);
        }
    }

    //更新类目
    @RequestMapping("/update")
    @ResponseBody
    public JSONObject update(Category c){
        logger.info("更新分类：" + c.getName());
        Integer row = categoryService.update(c);
        return resJson(row, "success", null);
    }

    //删除类目
    @RequestMapping("/delete")
    @ResponseBody
    public JSONObject delete(Integer id){
        logger.info("删除分类：" + id);
        Integer row = categoryService.delete(id);
        return resJson(row, "success", null);
    }
}
