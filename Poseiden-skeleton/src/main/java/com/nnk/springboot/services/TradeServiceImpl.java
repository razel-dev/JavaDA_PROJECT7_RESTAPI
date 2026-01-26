package com.nnk.springboot.services;


import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.mapper.TradeMapper;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {
    private final TradeRepository tradeRepository;
    private final TradeMapper tradeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TradeDto> findAll() {
        return tradeRepository.findAll()
                .stream()
                .map(tradeMapper::toDto)
                .toList();
    }

    @Override
      public TradeDto create(TradeDto dto) {
        dto.setId(null);
        Trade entity = tradeMapper.toEntity(dto);
        Trade saved = tradeRepository.save(entity);
        return tradeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TradeDto getDto(Integer id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trade introuvable avec l'id " + id));
        return tradeMapper.toDto(trade);
    }

    @Override
    public TradeDto update(Integer id, TradeDto dto) {
        Trade entity = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trade introuvable avec l'id " + id));
        tradeMapper.updateEntity(entity, dto);
        Trade saved = tradeRepository.save(entity);
        return tradeMapper.toDto(saved);
    }

    @Override
    public void delete(Integer id) {
        Trade entity = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trade introuvable avec l'id " + id));
        tradeRepository.delete(entity);
    }
}