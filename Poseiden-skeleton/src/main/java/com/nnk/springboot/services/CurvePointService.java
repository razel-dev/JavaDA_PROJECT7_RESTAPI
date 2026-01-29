package com.nnk.springboot.services;

import com.nnk.springboot.dto.CurvePointDto;

import java.util.List;

public interface CurvePointService {
List<CurvePointDto> findAll();
CurvePointDto create(CurvePointDto dto);
CurvePointDto getCurvePoint(Integer id);
CurvePointDto update(Integer id, CurvePointDto dto);
void delete(Integer id);
}