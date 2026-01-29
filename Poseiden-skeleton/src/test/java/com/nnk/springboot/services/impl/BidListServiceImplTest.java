package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.mapper.BidListMapper;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidListServiceImplTest {

    @Mock
    private BidListRepository bidListRepository;

    @Mock
    private BidListMapper bidListMapper;

    @InjectMocks
    private BidListServiceImpl service;

    private BidList entity1;
    private BidList entity2;
    private BidListDto dto1;
    private BidListDto dto2;

    @BeforeEach
    void setUp() {
        entity1 = sampleEntity(1, "acc1", "type1", new BigDecimal("10.0000"));
        entity2 = sampleEntity(2, "acc2", "type2", new BigDecimal("20.0000"));
        dto1 = sampleDto(1, "acc1", "type1", new BigDecimal("10.0000"));
        dto2 = sampleDto(2, "acc2", "type2", new BigDecimal("20.0000"));
    }

    @Test
    void findAll() {
        when(bidListRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(bidListMapper.toDto(entity1)).thenReturn(dto1);
        when(bidListMapper.toDto(entity2)).thenReturn(dto2);

        List<BidListDto> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("acc1", result.get(0).getAccount());
        assertEquals("acc2", result.get(1).getAccount());
        verify(bidListRepository).findAll();
        verify(bidListMapper).toDto(entity1);
        verify(bidListMapper).toDto(entity2);
        verifyNoMoreInteractions(bidListRepository, bidListMapper);
    }

    @Test
    void create() {
        BidListDto toCreate = sampleDto(null, "accX", "typeX", new BigDecimal("30.0000"));
        BidList mapped = sampleEntity(null, "accX", "typeX", new BigDecimal("30.0000"));
        BidList saved = sampleEntity(10, "accX", "typeX", new BigDecimal("30.0000"));
        BidListDto savedDto = sampleDto(10, "accX", "typeX", new BigDecimal("30.0000"));

        when(bidListMapper.toEntity(toCreate)).thenReturn(mapped);
        when(bidListRepository.save(mapped)).thenReturn(saved);
        when(bidListMapper.toDto(saved)).thenReturn(savedDto);

        BidListDto result = service.create(toCreate);

        assertNotNull(result.getId());
        assertEquals(10, result.getId());
        assertEquals("accX", result.getAccount());
        verify(bidListMapper).toEntity(toCreate);
        verify(bidListRepository).save(mapped);
        verify(bidListMapper).toDto(saved);
        verifyNoMoreInteractions(bidListRepository, bidListMapper);
    }

    @Test
    void getDto_ok() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(entity1));
        when(bidListMapper.toDto(entity1)).thenReturn(dto1);

        BidListDto result = service.getDto(1);

        assertEquals(1, result.getId());
        assertEquals("acc1", result.getAccount());
        verify(bidListRepository).findById(1);
        verify(bidListMapper).toDto(entity1);
        verifyNoMoreInteractions(bidListRepository, bidListMapper);
    }

    @Test
    void getDto_notFound() {
        when(bidListRepository.findById(999)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.getDto(999));
        assertTrue(ex.getMessage().contains("999"));
        verify(bidListRepository).findById(999);
        verifyNoMoreInteractions(bidListRepository);
        verifyNoInteractions(bidListMapper);
    }

    @Test
    void update_ok() {
        BidList existing = sampleEntity(5, "accOld", "typeOld", new BigDecimal("1.0000"));
        BidListDto patch = sampleDto(null, "accNew", "typeNew", new BigDecimal("2.5000"));
        BidList saved = sampleEntity(5, "accNew", "typeNew", new BigDecimal("2.5000"));
        BidListDto savedDto = sampleDto(5, "accNew", "typeNew", new BigDecimal("2.5000"));

        when(bidListRepository.findById(5)).thenReturn(Optional.of(existing));

        doAnswer(invocation -> {
            BidList target = invocation.getArgument(0, BidList.class);
            BidListDto src = invocation.getArgument(1, BidListDto.class);
            if (src.getAccount() != null) target.setAccount(src.getAccount());
            if (src.getType() != null) target.setType(src.getType());
            if (src.getBidQuantity() != null) target.setBidQuantity(src.getBidQuantity());
            return null;
        }).when(bidListMapper).updateEntity(eq(existing), eq(patch));

        when(bidListRepository.save(existing)).thenReturn(saved);
        when(bidListMapper.toDto(saved)).thenReturn(savedDto);

        BidListDto result = service.update(5, patch);

        assertEquals(5, result.getId());
        assertEquals("accNew", result.getAccount());
        assertEquals("typeNew", result.getType());
        assertEquals(new BigDecimal("2.5000"), result.getBidQuantity());

        verify(bidListRepository).findById(5);
        verify(bidListMapper).updateEntity(existing, patch);
        verify(bidListRepository).save(existing);
        verify(bidListMapper).toDto(saved);
        verifyNoMoreInteractions(bidListRepository, bidListMapper);
    }

    @Test
    void update_notFound() {
        when(bidListRepository.findById(123)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.update(123, new BidListDto()));
        assertTrue(ex.getMessage().contains("123"));
        verify(bidListRepository).findById(123);
        verifyNoMoreInteractions(bidListRepository);
        verifyNoInteractions(bidListMapper);
    }

    @Test
    void delete_ok() {
        when(bidListRepository.existsById(7)).thenReturn(true);

        service.delete(7);

        verify(bidListRepository).existsById(7);
        verify(bidListRepository).deleteById(7);
        verifyNoMoreInteractions(bidListRepository);
    }

    @Test
    void delete_notFound() {
        when(bidListRepository.existsById(404)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.delete(404));
        assertTrue(ex.getMessage().contains("404"));

        verify(bidListRepository).existsById(404);
        verifyNoMoreInteractions(bidListRepository);
    }



    private static BidList sampleEntity(Integer id, String account, String type, BigDecimal qty) {
        BidList b = new BidList();
        b.setId(id);
        b.setAccount(account);
        b.setType(type);
        b.setBidQuantity(qty);
        return b;
    }

    private static BidListDto sampleDto(Integer id, String account, String type, BigDecimal qty) {
        BidListDto d = new BidListDto();
        d.setId(id);
        d.setAccount(account);
        d.setType(type);
        d.setBidQuantity(qty);
        return d;
    }
}