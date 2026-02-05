package com.nnk.springboot.services.impl;

import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.mapper.RatingMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

import com.nnk.springboot.services.RatingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implémentation de l'interface RatingService, fournissant les méthodes de gestion
 * des entités  Rating et de leurs objets de transfert de données (DTO).
 * Cette classe prend en charge les opérations de lecture, création, mise à jour et suppression.
 *
 * Annotations utilisées :
 * - @Slf4j : fournit un logger pour la journalisation.
 * - @Service : déclare le composant en tant que service Spring.
 * - @RequiredArgsConstructor : génère un constructeur injectant les dépendances requises.
 * - @Transactional : gère les transactions au niveau du service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    /**
     * Récupère toutes les entités Rating depuis le dépôt, les convertit en RatingDto
     * et retourne la liste résultante.
     *
     * La méthode journalise le démarrage, le nombre d’éléments récupérés et le temps écoulé en millisecondes.
     *
     * @return la liste des RatingDto représentant toutes les notations stockées.
     */
    @Override
    public List<RatingDto> findAll() {
        long t0 = System.nanoTime();
        log.info("Liste des notations (Rating) - démarrage");
        List<RatingDto> result = ratingRepository.findAll()
                .stream()
                .map(ratingMapper::toDto)
                .toList();
        log.info("Liste récupérée: {} élément(s) en {} ms", result.size(), (System.nanoTime() - t0) / 1_000_000);
        return result;
    }


    /**
     * Récupère une entité Rating par son identifiant, la convertit en RatingDto
     * puis la retourne.
     * <p>
     * Si aucune entité n’est trouvée, uneEntityNotFoundException est levée.
     *
     * @param id identifiant de la notation à récupérer
     * @return le RatingDto correspondant à l’entité trouvée
     * @throws EntityNotFoundException si aucune notation n’est trouvée pour l’identifiant fourni
     */
    @Transactional(readOnly = true)
    @Override
    public RatingDto getRating(Integer id) {
        log.debug("Récupération de la notation id={}", id);
        Rating entity = ratingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notation introuvable pour id={}", id);
                    return new EntityNotFoundException("Rating introuvable pour l'id=" + id);
                });
        return ratingMapper.toDto(entity);
    }


    /**
     * Crée une nouvelle entité Rating à partir du RatingDto fourni, l’enregistre
     * dans le dépôt, puis retourne sa représentation RatingDto.
     *
     * La méthode journalise le processus de création (principaux champs du DTO) et confirme la réussite
     * avec l’identifiant généré.
     *
     * @param dto le DTO contenant les informations de la notation à créer
     * @return le RatingDto représentant l’entité nouvellement créée
     */
    @Override
    @Transactional
    public RatingDto create(RatingDto dto) {
        log.info("Création d'une notation: moodys='{}' sandp='{}' fitch='{}' ordre={}",
                dto.getMoodysRating(), dto.getSandpRating(), dto.getFitchRating(), dto.getOrderNumber());
        Rating entity = ratingMapper.toEntity(dto);
        Rating saved = ratingRepository.save(entity);
        log.info("Notation créée avec succès: id={}", saved.getId());
        return ratingMapper.toDto(saved);
    }

    /**
     * Met à jour une entité Rating existante identifiée par son identifiant, à partir des données
     * fournies dans le RatingDto, puis retourne le RatingDto mis à jour.
     *
     * Les champs nuls du DTO sont ignorés lors de la mise à jour (mise à jour partielle).
     * Si l’entité n’existe pas, une EntityNotFoundException est levée.
     *
     * @param id  identifiant de la notation à mettre à jour
     * @param dto le DTO contenant les nouvelles valeurs
     * @return le RatingDto correspondant à l’entité mise à jour
     * @throws EntityNotFoundException si aucune notation n’est trouvée pour l’identifiant fourni
     */
    @Override
    @Transactional
    public RatingDto update(Integer id, RatingDto dto) {
        log.info("Mise à jour de la notation id={} (moodys='{}' sandp='{}' fitch='{}' ordre={})",
                id, dto.getMoodysRating(), dto.getSandpRating(), dto.getFitchRating(), dto.getOrderNumber());
        Rating entity = ratingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Mise à jour impossible: notation introuvable pour id={}", id);
                    return new EntityNotFoundException("Rating introuvable pour l'id=" + id);
                });
        ratingMapper.updateEntity(entity, dto);
        Rating saved = ratingRepository.save(entity);
        log.info("Notation mise à jour: id={}", saved.getId());
        return ratingMapper.toDto(saved);
    }


    /**
     * Supprime une entité Rating identifiée par son identifiant depuis le dépôt.
     *
     * La méthode vérifie d’abord l’existence de l’entité. Si elle n’existe pas, une
     *  EntityNotFoundException est levée. Le processus de suppression est
     * journalisé, y compris un avertissement le cas échéant.
     *
     * @param id identifiant de la notation à supprimer
     * @throws EntityNotFoundException si aucune notation n’est trouvée pour l’identifiant fourni
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Suppression de la notation id={}", id);
        if (!ratingRepository.existsById(id)) {
            log.warn("Suppression impossible: notation introuvable pour id={}", id);
            throw new EntityNotFoundException("Rating introuvable pour l'id=" + id);
        }
        ratingRepository.deleteById(id);
        log.info("Notation supprimée: id={}", id);
    }
}