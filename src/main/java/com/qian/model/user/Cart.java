package com.qian.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class Cart {
    private Integer id;
    private Integer uId;//用户id
    private Integer gId;//商品id
    private Integer buyCount;//购买数量
    private Timestamp createTime;//创建时间
    private String gName;//商品名称
    private String gImage;//商品图片
    private BigDecimal gPrice;//商品价格
    private BigDecimal totalPrice;//商品价格
}
