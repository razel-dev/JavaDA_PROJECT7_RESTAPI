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

/**
 * Implémentation du service métier gérant les transactions (Trade).
 *
 * Cette classe expose des opérations CRUD sur les entités Trade, tout en
 * assurant la conversion avec leurs représentations de transfert (DTO)
 * via un mapper dédié, afin de garantir une séparation claire des couches.
 *
 * Annotations et comportement :
 * - @Service : déclare la classe comme composant de service Spring.
 * - @Transactional : rend l'ensemble des méthodes transactionnelles par défaut.
 * - @RequiredArgsConstructor : génère un constructeur pour l'injection des dépendances requises.
 * - @Slf4j : fournit un logger pour tracer les opérations.
 *
 * Dépendances principales :
 * - TradeRepository : accès aux opérations de persistance sur les entités Trade.
 * - TradeMapper : conversions entité <-> DTO et mises à jour partielles.
 *
 * Journalisation :
 * Toutes les opérations sont journalisées (démarrage, succès, erreurs fonctionnelles),
 * et certaines incluent des mesures simples de performance (durée d'exécution).
 *
 */
@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class TradeServiceImpl implements TradeService {
    private final TradeRepository tradeRepository;
    private final TradeMapper tradeMapper;

    /**
     * Récupère toutes les entités Trade en base, les convertit en TradeDto
     * et retourne la liste résultante.
     *
     * L'opération est transactionnelle en lecture seule pour favoriser la cohérence
     * et de possibles optimisations. La méthode journalise le nombre d’éléments
     * récupérés ainsi que le temps d'exécution.
     *
     * @return la liste des TradeDto représentant tous les trades enregistrés
     */
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

    /**
     * Crée une nouvelle entité Trade à partir du DTO fourni, la persiste en base,
     * puis retourne l'entité sauvegardée sous forme de DTO.
     *
     * Remarques :
     * - L'identifiant éventuellement présent dans le DTO d'entrée est ignoré (forcé à null)
     *   afin de laisser la base générer la clé primaire.
     * - Les informations principales (compte, type, quantité d'achat) sont journalisées.
     *
     * @param dto le TradeDto contenant les informations du trade à créer
     * @return le TradeDto correspondant à l'entité nouvellement persistée (avec identifiant généré)
     */
    @Override
    public TradeDto create(TradeDto dto) {
        log.info("Création d'un trade: compte={} type={} quantitéAchat={}", dto.getAccount(), dto.getType(), dto.getBuyQuantity());
        dto.setId(null);
        Trade entity = tradeMapper.toEntity(dto);
        Trade saved = tradeRepository.save(entity);
        log.info("Trade créé avec succès: id={}", saved.getId());
        return tradeMapper.toDto(saved);
    }

    /**
     * Récupère un trade par son identifiant et le convertit en DTO.
     * Si aucun trade n'est trouvé, une IllegalArgumentException est levée.
     *
     * @param id l'identifiant unique du trade à récupérer
     * @return le TradeDto correspondant au trade trouvé
     * @throws IllegalArgumentException si aucun trade n'existe pour l'identifiant fourni
     */
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

    /**
     * Met à jour un trade existant identifié par son id avec les données fournies
     * dans le DTO, puis retourne le TradeDto mis à jour.
     *
     * Comportement :
     * - Si le trade n'existe pas, une IllegalArgumentException est levée.
     * - Les champs nuls du DTO sont ignorés (mise à jour partielle) grâce au mapper.
     *
     * @param id  l'identifiant du trade à mettre à jour
     * @param dto le TradeDto contenant les nouvelles valeurs à appliquer
     * @return le TradeDto représentant le trade après mise à jour
     * @throws IllegalArgumentException si aucun trade n'est trouvé pour l'identifiant donné
     */
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

    /**
     * Supprime le trade identifié par son identifiant unique.
     * Si le trade n'existe pas, une IllegalArgumentException est levée.
     *
     * @param id l'identifiant du trade à supprimer
     * @throws IllegalArgumentException si aucun trade n'existe pour l'identifiant fourni
     */
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