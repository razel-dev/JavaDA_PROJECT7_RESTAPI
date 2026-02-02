package com.nnk.springboot.services.impl;


import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.mapper.BidListMapper;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BidListServiceImpl implements BidListService {
    private final BidListRepository bidListRepository;
    private final BidListMapper bidListMapper;

    @Transactional(readOnly = true)
    @Override
    public List<BidListDto> findAll() {
        long t0 = System.nanoTime();
        log.info("Liste des enchères (BidList) - démarrage");
        List<BidListDto> result = bidListRepository.findAll()
                .stream()
                .map(bidListMapper::toDto)
                .toList();
        log.info("Liste récupérée: {} élément(s) en {} ms", result.size(), (System.nanoTime() - t0) / 1_000_000);
        return result;
    }

    @Override
    public BidListDto create(BidListDto dto) {
        log.info("Création d'une enchère: compte={} type={} quantité={}", dto.getAccount(), dto.getType(), dto.getBidQuantity());
        BidList entity = bidListMapper.toEntity(dto);
        BidList saved = bidListRepository.save(entity);
        log.info("Enchère créée avec succès: id={}", saved.getId());
        return bidListMapper.toDto(saved);
    }
    @Transactional(readOnly = true)
    @Override
    public BidListDto getBidList (Integer id) {
        log.debug("Récupération de l'enchère id={}", id);
        BidList entity = bidListRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Enchère introuvable pour id={}", id);
                    return new IllegalArgumentException("BidList introuvable avec l'id " + id);
                });

        return bidListMapper.toDto(entity);
    }

    @Override
    public BidListDto update(Integer id, BidListDto dto) {
        log.info("Mise à jour de l'enchère id={} (compte={} type={} quantité={})", id, dto.getAccount(), dto.getType(), dto.getBidQuantity());
        BidList entity = bidListRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Mise à jour impossible: enchère introuvable pour id={}", id);
                    return new IllegalArgumentException("BidList introuvable avec l'id " + id);
                });
        bidListMapper.updateEntity(entity, dto);
        BidList saved = bidListRepository.save(entity);
        log.info("Enchère mise à jour: id={}", saved.getId());
        return bidListMapper.toDto(saved);
    }

    @Override
    public void delete(Integer id) {
        log.info("Suppression de l'enchère id={}", id);
        if (!bidListRepository.existsById(id)) {
            log.warn("Suppression impossible: enchère introuvable pour id={}", id);
            throw new IllegalArgumentException("BidList introuvable avec l'id " + id);

        }
        bidListRepository.deleteById(id);
        log.info("Enchère supprimée: id={}", id);
    }
}