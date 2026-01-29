package com.nnk.springboot.services;


import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;

import java.util.List;

public interface TradeService {
    List<TradeDto> findAll();
    TradeDto create(TradeDto dto);
    TradeDto getTrade(Integer id);
    TradeDto update(Integer id, TradeDto dto);
    void delete(Integer id);
}