package com.qian.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class Trade {
    private Integer id;//交易流水id
    private Integer uId;//用户id
    private String orderNo;//订单号
    private BigDecimal tradeMoney;//交易金额
    private Integer tradeType;//交易类型 1-支付 2-充值
}
