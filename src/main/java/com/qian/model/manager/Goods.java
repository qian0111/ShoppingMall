package com.qian.model.manager;

import java.math.BigDecimal;

public class Goods {
    private Integer id;//商品id
    private String gName;//商品名称
    private String gImage;//商品图片
    private BigDecimal gPrice;//商品价格
    private Integer gCount;//商品库存
    private Integer gStatus;//1-上架，2-下架
    private Integer cId;//分类id
    private String cName;//分类名
    private Integer parentId;//一级分类id
    private String parentName;//一级分类名
    private Integer pageNo;//分页：第n页
    private Integer pageCount;//分页：每页页数

    public Goods() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgImage() {
        return gImage;
    }

    public void setgImage(String gImage) {
        this.gImage = gImage;
    }

    public BigDecimal getgPrice() {
        return gPrice;
    }

    public void setgPrice(BigDecimal gPrice) {
        this.gPrice = gPrice;
    }

    public Integer getgCount() {
        return gCount;
    }

    public void setgCount(Integer gCount) {
        this.gCount = gCount;
    }

    public Integer getgStatus() {
        return gStatus;
    }

    public void setgStatus(Integer gStatus) {
        this.gStatus = gStatus;
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
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
        return "Goods{" +
                "id=" + id +
                ", gName='" + gName + '\'' +
                ", gImage='" + gImage + '\'' +
                ", gPrice=" + gPrice +
                ", gCount=" + gCount +
                ", gStatus=" + gStatus +
                ", cId=" + cId +
                ", cName='" + cName + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", pageNo=" + pageNo +
                ", pageCount=" + pageCount +
                '}';
    }
}