package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.mapper.CurvePointMapper;
import com.nnk.springboot.repositories.CurvePointRepository;

import com.nnk.springboot.services.CurvePointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class CurvePointServiceImpl implements CurvePointService {
    private final CurvePointRepository curvePointRepository;
    private final CurvePointMapper curvePointMapper;
   ;

    @Transactional(readOnly = true)
    public List<CurvePointDto> findAll() {
        return curvePointRepository.findAll()
                .stream()
                .map(curvePointMapper::toDto)
                .toList();
    }

    @Override
    public CurvePointDto create(CurvePointDto dto) {
        CurvePoint entity = curvePointMapper.toEntity(dto);
        CurvePoint saved = curvePointRepository.save(entity);
        return curvePointMapper.toDto(saved);
    }
    @Transactional (readOnly = true)
    @Override
    public CurvePointDto getDto(Integer id) {
        CurvePoint entity = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CurvePoint introuvable avec l'id " + id));

        return curvePointMapper.toDto(entity);
    }

    @Override
    public CurvePointDto update(Integer id, CurvePointDto dto) {
        CurvePoint entity = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CurvePoint introuvable avec l'id " + id));
        curvePointMapper.updateEntity(entity, dto);
        CurvePoint saved = curvePointRepository.save(entity);
        return curvePointMapper.toDto(saved);
    }

    @Override
    public void delete(Integer id) {
        if (!curvePointRepository.existsById(id)) {
            throw new IllegalArgumentException("CurvePoint introuvable avec l'id " + id);
        }
        curvePointRepository.deleteById(id);

    }
}
