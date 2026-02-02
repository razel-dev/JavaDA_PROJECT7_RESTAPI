package com.nnk.springboot.services.impl;


import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.mapper.TradeMapper;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class TradeServiceImpl implements TradeService {
    private final TradeRepository tradeRepository;
    private final TradeMapper tradeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TradeDto> findAll() {
        long t0 = System.nanoTime();
        log.info("Liste des trades - démarrage");
        List<TradeDto> result = tradeRepository.findAll()
                .stream()
                .map(tradeMapper::toDto)
                .toList();
        log.info("Liste récupérée: {} élément(s) en {} ms", result.size(), (System.nanoTime() - t0) / 1_000_000);
        return result;
    }

    @Override
    public TradeDto create(TradeDto dto) {
        log.info("Création d'un trade: compte={} type={} quantitéAchat={}", dto.getAccount(), dto.getType(), dto.getBuyQuantity());
        dto.setId(null);
        Trade entity = tradeMapper.toEntity(dto);
        Trade saved = tradeRepository.save(entity);
        log.info("Trade créé avec succès: id={}", saved.getId());
        return tradeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TradeDto getTrade(Integer id) {
        log.debug("Récupération du trade id={}", id);
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Trade introuvable pour id={}", id);
                    return new IllegalArgumentException("Trade introuvable avec l'id " + id);
                });
        return tradeMapper.toDto(trade);
    }

    @Override
    public TradeDto update(Integer id, TradeDto dto) {
        log.info("Mise à jour du trade id={} (compte={} type={} quantitéAchat={})", id, dto.getAccount(), dto.getType(), dto.getBuyQuantity());
        Trade entity = tradeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Mise à jour impossible: trade introuvable pour id={}", id);
                    return new IllegalArgumentException("Trade introuvable avec l'id " + id);
                });
        tradeMapper.updateEntity(entity, dto);
        Trade saved = tradeRepository.save(entity);
        log.info("Trade mis à jour: id={}", saved.getId());
        return tradeMapper.toDto(saved);
    }

    @Override
    public void delete(Integer id) {
        log.info("Suppression du trade id={}", id);
        Trade entity = tradeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Suppression impossible: trade introuvable pour id={}", id);
                    return new IllegalArgumentException("Trade introuvable avec l'id " + id);
                });
        tradeRepository.delete(entity);
        log.info("Trade supprimé: id={}", id);
    }
}