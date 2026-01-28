package com.nnk.springboot.services;


import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.mapper.BidListMapper;
import com.nnk.springboot.repositories.BidListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class BidListServiceImpl implements BidListService{
    private final BidListRepository bidListRepository;
    private final BidListMapper bidListMapper;


    @Transactional(readOnly = true)
    @Override
    public List<BidListDto> findAll() {

        return bidListRepository.findAll()
                .stream()
                .map(bidListMapper::toDto)
                .toList();
    }

    @Override
    public BidListDto create(BidListDto dto) {
        BidList entity = bidListMapper.toEntity(dto);
        BidList saved = bidListRepository.save(entity);
        return bidListMapper.toDto(saved);
    }
    @Transactional(readOnly = true)
    @Override
    public BidListDto getDto(Integer id) {
        BidList entity = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BidList introuvable avec l'id " + id));

        return bidListMapper.toDto(entity);
    }

    @Override
    public BidListDto update(Integer id, BidListDto dto) {
        BidList entity = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BidList introuvable avec l'id " + id));
        bidListMapper.updateEntity(entity, dto);
        BidList saved = bidListRepository.save(entity);
        return bidListMapper.toDto(saved);
    }

    @Override
    public void delete(Integer id) {
        if (!bidListRepository.existsById(id)) {
            throw new IllegalArgumentException("BidList introuvable avec l'id " + id);

        }
        bidListRepository.deleteById(id);
    }
}