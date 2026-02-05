package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.mapper.RuleNameMapper;
import com.nnk.springboot.repositories.RuleNameRepository;

import com.nnk.springboot.services.RuleNameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implémentation de l'interface RuleNameService gérant la logique métier
 * liée aux entités RuleName.
 *
 * Cette classe expose des opérations pour créer, lire, mettre à jour,
 * supprimer et lister les règles. Elle interagit avec la base de données
 * via RuleNameRepository et réalise les conversions entité/DTO avec RuleNameMapper.
 *
 * La classe est transactionnelle afin d'assurer la cohérence des données
 * et utilise un journal applicatif pour tracer les opérations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RuleNameServiceImpl implements RuleNameService {

    private final RuleNameRepository ruleNameRepository;
    private final RuleNameMapper ruleNameMapper;

    /**
     * Récupère la liste de toutes les entités RuleName en base,
     * les convertit en DTO, puis retourne le résultat.
     *
     * L'opération est transactionnelle en lecture seule afin d'assurer
     * la cohérence et de possibles optimisations. Des logs indiquent
     * le démarrage, le nombre d'éléments récupérés et le temps d'exécution.
     *
     * @return la liste des RuleNameDto représentant toutes les règles enregistrées
     */
    @Override
    @Transactional(readOnly = true)
    public List<RuleNameDto> findAll() {
        long t0 = System.nanoTime();
        log.info("Liste des règles (RuleName) - démarrage");
        List<RuleNameDto> result = ruleNameRepository.findAll()
                .stream()
                .map(ruleNameMapper::toDto)
                .toList();
        log.info("Liste récupérée: {} élément(s) en {} ms", result.size(), (System.nanoTime() - t0) / 1_000_000);
        return result;
    }

    /**
     * Crée une nouvelle règle à partir du DTO fourni, la persiste en base
     * puis retourne l'entité sauvegardée sous forme de DTO.
     *
     * L'identifiant éventuellement présent dans le DTO d'entrée est ignoré
     * (forcé à null) afin de laisser la base générer la clé primaire.
     * Des logs tracent la demande de création et le succès avec l'identifiant.
     *
     * @param dto le DTO contenant les informations de la règle à créer
     * @return le RuleNameDto correspondant à l'entité nouvellement persistée
     */
    @Override
    public RuleNameDto create(RuleNameDto dto) {
        log.info("Création d'une règle: name='{}' description='{}'", dto.getName(), dto.getDescription());
        dto.setId(null);
        RuleName entity = ruleNameMapper.toEntity(dto);
        RuleName saved = ruleNameRepository.save(entity);
        log.info("Règle créée avec succès: id={}", saved.getId());
        return ruleNameMapper.toDto(saved);
    }

    /**
     * Récupère une règle par son identifiant et la convertit en DTO.
     * Lève une IllegalArgumentException si aucune entité n'est trouvée.
     *
     * @param id l'identifiant unique de la règle à récupérer
     * @return le RuleNameDto correspondant à l'entité trouvée
     * @throws IllegalArgumentException si aucune règle n'existe pour l'identifiant fourni
     */
    @Override
    @Transactional(readOnly = true)
    public RuleNameDto getRuleName(Integer id) {
        log.debug("Récupération de la règle id={}", id);
        RuleName entity = ruleNameRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Règle introuvable pour id={}", id);
                    return new IllegalArgumentException("RuleName introuvable avec l'id " + id);
                });
        return ruleNameMapper.toDto(entity);
    }

    /**
     * Met à jour une règle existante avec les données fournies dans le DTO.
     * Si l'entité n'existe pas, lève une IllegalArgumentException.
     * Seuls les champs non nuls du DTO écrasent les valeurs de l'entité.
     *
     * @param id  l'identifiant de la règle à mettre à jour
     * @param dto le DTO contenant les modifications à appliquer
     * @return le RuleNameDto représentant l'entité mise à jour
     * @throws IllegalArgumentException si aucune règle n'est trouvée pour l'identifiant donné
     */
    @Override
    public RuleNameDto update(Integer id, RuleNameDto dto) {
        log.info("Mise à jour de la règle id={} (name='{}' description='{}')", id, dto.getName(), dto.getDescription());
        RuleName entity = ruleNameRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Mise à jour impossible: règle introuvable pour id={}", id);
                    return new IllegalArgumentException("RuleName introuvable avec l'id " + id);
                });
        ruleNameMapper.updateEntity(entity, dto);
        RuleName saved = ruleNameRepository.save(entity);
        log.info("Règle mise à jour: id={}", saved.getId());
        return ruleNameMapper.toDto(saved);
    }

    /**
     * Supprime la règle identifiée par son identifiant unique.
     * Lève une IllegalArgumentException si aucune entité correspondante n'est trouvée.
     *
     * @param id l'identifiant de la règle à supprimer
     * @throws IllegalArgumentException si aucune règle n'existe pour l'identifiant fourni
     */
    @Override
    public void delete(Integer id) {
        log.info("Suppression de la règle id={}", id);
        RuleName entity = ruleNameRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Suppression impossible: règle introuvable pour id={}", id);
                    return new IllegalArgumentException("RuleName introuvable avec l'id " + id);
                });
        ruleNameRepository.delete(entity);
        log.info("Règle supprimée: id={}", id);
    }
}