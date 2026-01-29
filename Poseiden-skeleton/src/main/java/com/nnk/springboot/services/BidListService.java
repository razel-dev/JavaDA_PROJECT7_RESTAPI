package com.nnk.springboot.services;


import com.nnk.springboot.dto.BidListDto;

import java.util.List;

public interface BidListService {
    List<BidListDto> findAll();
    BidListDto create(BidListDto dto);
    BidListDto getBidList(Integer id);
    BidListDto update(Integer id, BidListDto dto);
    void delete(Integer id);
}