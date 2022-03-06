package com.qian.model.manager;

public class Manager {
    Integer id; //用户id，主键
    String mName; //用户名
    String mPass; //密码

    public Manager() {

    }

    public Manager(Integer id, String mName, String mPass) {
        this.id = id;
        this.mName = mName;
        this.mPass = mPass;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPass() {
        return mPass;
    }

    public void setmPass(String mPass) {
        this.mPass = mPass;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", mName='" + mName + '\'' +
                ", mPass='" + mPass + '\'' +
                '}';
    }
}
