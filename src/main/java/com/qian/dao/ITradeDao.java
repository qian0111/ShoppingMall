package com.qian.dao;

import com.qian.model.user.Trade;
import org.springframework.stereotype.Repository;

@Repository
public interface ITradeDao {
    //创建交易流水
    public int insert(Trade trade);
}
