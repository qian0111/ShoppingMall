package com.qian.model.manager;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {
    private String orderNo;//订单号
    private Integer uId;//用户id
    private Integer gId;//商品id
    private String gName;//商品名称
    private String gImage;//商品图片
    private BigDecimal gPrice;//商品价格
    private Integer buyCount;//购买数量
    private Timestamp updateTime;//更新时间
    private Timestamp createTime;//创建时间
    private BigDecimal payMoney;//支付金额
    private Integer orderStatus;//订单状态
    private Integer pageNo;//分页：第n页
    private Integer pageCount;//分页：每页页数

    private Integer orderStatus2;//备用：订单状态
    private String statusName;//解释状态码

    public Order() {
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getgImage() {
        return gImage;
    }

    public void setgImage(String gImage) {
        this.gImage = gImage;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getOrderStatus2() {
        return orderStatus2;
    }

    public void setOrderStatus2(Integer orderStatus2) {
        this.orderStatus2 = orderStatus2;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getgId() {
        return gId;
    }

    public void setgId(Integer gId) {
        this.gId = gId;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public BigDecimal getgPrice() {
        return gPrice;
    }

    public void setgPrice(BigDecimal gPrice) {
        this.gPrice = gPrice;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNo='" + orderNo + '\'' +
                ", uId=" + uId +
                ", gId=" + gId +
                ", gName='" + gName + '\'' +
                ", gPrice=" + gPrice +
                ", buyCount=" + buyCount +
                ", payMoney=" + payMoney +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
