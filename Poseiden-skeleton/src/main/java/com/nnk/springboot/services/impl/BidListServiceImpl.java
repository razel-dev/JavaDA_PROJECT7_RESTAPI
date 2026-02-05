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

/**
 * Implémentation du service pour la gestion des entités BidList.
 * Cette classe fournit des méthodes pour effectuer des opérations CRUD
 * ainsi que des transformations de données pour BidList.
 *
 * Fonctionnalités :
 * - Récupérer toutes les entrées BidList.
 * - Créer une nouvelle entrée BidList.
 * - Récupérer une entrée BidList par son identifiant.
 * - Mettre à jour une entrée BidList existante.
 * - Supprimer une entrée BidList par son identifiant.
 *
 * Ce service utilise BidListRepository pour les opérations en base de données
 * et BidListMapper pour la conversion entre entité et DTO.
 *
 * Toutes les méthodes sont transactionnelles afin d’assurer la cohérence des opérations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BidListServiceImpl implements BidListService {
    private final BidListRepository bidListRepository;
    private final BidListMapper bidListMapper;

    /**
     * Récupère toutes les entrées BidList depuis la base de données,
     * les mappe vers leurs représentations DTO, puis renvoie la liste complète.
     *
     * @return une liste de BidListDto contenant toutes les entrées BidList.
     */
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



    /**
     * Crée une nouvelle entrée BidList, l’enregistre en base de données,
     * puis renvoie sa représentation au format DTO.
     *
     * @param dto le DTO contenant les informations de l’entrée BidList à créer.
     * @return un BidListDto représentant l’entrée BidList créée.
     */
    @Override
    public BidListDto create(BidListDto dto) {
        log.info("Création d'une enchère: compte={} type={} quantité={}", dto.getAccount(), dto.getType(), dto.getBidQuantity());
        BidList entity = bidListMapper.toEntity(dto);
        BidList saved = bidListRepository.save(entity);
        log.info("Enchère créée avec succès: id={}", saved.getId());
        return bidListMapper.toDto(saved);
    }

    /**
     * Récupère une entrée BidList par son identifiant, la convertit en DTO
     * puis la renvoie.
     *
     * @param id l’identifiant de l’entrée BidList à récupérer.
     * @return un BidListDto contenant les informations de l’entrée demandée.
     * @throws IllegalArgumentException si aucune entrée BidList n’est trouvée pour l’identifiant fourni.
     */
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


    /**
     * Met à jour une entrée BidList existante identifiée par son identifiant
     * à partir des données fournies dans le DTO. L’entrée est d’abord récupérée,
     * les modifications sont appliquées, puis sauvegardées, et enfin le DTO mis à jour est renvoyé.
     *
     * @param id l’identifiant de l’entrée BidList à mettre à jour.
     * @param dto le DTO contenant les informations de mise à jour pour l’entrée BidList.
     * @return un BidListDto représentant l’entrée BidList mise à jour.
     * @throws IllegalArgumentException si aucune entrée BidList n’est trouvée pour l’identifiant fourni.
     */
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


    /**
     * Supprime une entrée BidList par son identifiant.
     * Si l’identifiant spécifié n’existe pas en base, une exception est levée
     * indiquant que l’entité est introuvable.
     *
     * @param id l’identifiant de l’entrée BidList à supprimer.
     * @throws IllegalArgumentException si aucune entrée BidList n’est trouvée pour l’identifiant fourni.
     */
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