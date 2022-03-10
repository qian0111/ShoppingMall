package com.qian.model.manager;
/*
商品类目实体类
 */
public class Category {
    private Integer id; //类目ID
    private String name; //类目名称
    private Integer parentId; //父类ID
    private Integer level; //类目级别
    private Integer status; //类目状态：1-启用 2-禁用

    public Category(){

    }

    public Category(Integer parentId){
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
