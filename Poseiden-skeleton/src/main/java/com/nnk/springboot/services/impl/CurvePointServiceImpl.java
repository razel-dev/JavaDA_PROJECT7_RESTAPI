package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.mapper.CurvePointMapper;
import com.nnk.springboot.repositories.CurvePointRepository;

import com.nnk.springboot.services.CurvePointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implémentation du service pour la gestion des entités CurvePoint.
 * Cette classe fournit des opérations CRUD et s’appuie sur:
 * - CurvePointRepository pour l’accès aux données,
 * - CurvePointMapper pour la conversion entité ↔ DTO et la mise à jour partielle.
 *
 * Comportement transactionnel:
 * - Les méthodes de lecture sont annotées readOnly = true.
 * - Les méthodes d’écriture (création, mise à jour, suppression) participent à une transaction par défaut.
 *
 * Journalisation:
 * - Utilise SLF4J pour tracer les opérations (info/debug/warn) et mesurer le temps d’exécution de findAll().
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CurvePointServiceImpl implements CurvePointService {
    private final CurvePointRepository curvePointRepository;
    private final CurvePointMapper curvePointMapper;

    /**
     * Récupère toutes les entrées CurvePoint depuis la base de données,
     * les convertit en DTO, puis renvoie la liste complète.
     * La méthode mesure et journalise le temps de traitement.
     *
     * @return une liste de CurvePointDto représentant toutes les entrées CurvePoint.
     */
    @Transactional(readOnly = true)
    @Override
    public List<CurvePointDto> findAll() {
        long t0 = System.nanoTime();
        log.info("Liste des points de courbe (CurvePoint) - démarrage");
        List<CurvePointDto> result = curvePointRepository.findAll()
                .stream()
                .map(curvePointMapper::toDto)
                .toList();
        log.info("Liste récupérée: {} élément(s) en {} ms", result.size(), (System.nanoTime() - t0) / 1_000_000);
        return result;
    }



    /**
     * Crée un nouveau CurvePoint à partir du DTO fourni, persiste l’entité,
     * puis renvoie sa représentation DTO.
     *
     * @param dto le DTO contenant les informations nécessaires à la création.
     * @return le CurvePointDto correspondant à l’entité créée.
     */
    @Override
    public CurvePointDto create(CurvePointDto dto) {
        log.info("Création d'un point de courbe: curveId={} term={} value={}", dto.getCurveId(), dto.getTerm(), dto.getValue());
        CurvePoint entity = curvePointMapper.toEntity(dto);
        CurvePoint saved = curvePointRepository.save(entity);
        log.info("Point de courbe créé avec succès: id={}", saved.getId());
        return curvePointMapper.toDto(saved);
    }


    /**
     * Récupère un CurvePoint par son identifiant, le convertit en DTO et le renvoie.
     *
     * @param id l’identifiant du CurvePoint à récupérer.
     * @return le CurvePointDto correspondant.
     * @throws IllegalArgumentException si aucun CurvePoint n’est trouvé pour l’identifiant fourni.
     */
    @Transactional(readOnly = true)
    @Override
    public CurvePointDto getCurvePoint(Integer id) {
        log.debug("Récupération du point de courbe id={}", id);
        CurvePoint entity = curvePointRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Point de courbe introuvable pour id={}", id);
                    return new IllegalArgumentException("CurvePoint introuvable avec l'id " + id);
                });
        return curvePointMapper.toDto(entity);
    }



    /**
     * Met à jour un CurvePoint existant identifié par son identifiant à partir des
     * données fournies dans le DTO. Les propriétés nulles du DTO sont ignorées
     * (mise à jour partielle).
     *
     * @param id  l’identifiant du CurvePoint à mettre à jour.
     * @param dto le DTO contenant les nouvelles valeurs à appliquer.
     * @return le CurvePointDto mis à jour.
     * @throws IllegalArgumentException si aucun CurvePoint n’est trouvé pour l’identifiant fourni.
     */
    @Override
    public CurvePointDto update(Integer id, CurvePointDto dto) {
        log.info("Mise à jour du point de courbe id={} (curveId={} term={} value={})", id, dto.getCurveId(), dto.getTerm(), dto.getValue());
        CurvePoint entity = curvePointRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Mise à jour impossible: point de courbe introuvable pour id={}", id);
                    return new IllegalArgumentException("CurvePoint introuvable avec l'id " + id);
                });
        curvePointMapper.updateEntity(entity, dto);
        CurvePoint saved = curvePointRepository.save(entity);
        log.info("Point de courbe mis à jour: id={}", saved.getId());
        return curvePointMapper.toDto(saved);
    }



    /**
     * Supprime un CurvePoint par son identifiant. Si l’identifiant n’existe pas,
     * une exception est levée.
     *
     * @param id l’identifiant du CurvePoint à supprimer.
     * @throws IllegalArgumentException si aucun CurvePoint n’est trouvé pour l’identifiant fourni.
     */
    @Override
    public void delete(Integer id) {
        log.info("Suppression du point de courbe id={}", id);
        if (!curvePointRepository.existsById(id)) {
            log.warn("Suppression impossible: point de courbe introuvable pour id={}", id);
            throw new IllegalArgumentException("CurvePoint introuvable avec l'id " + id);
        }
        curvePointRepository.deleteById(id);
        log.info("Point de courbe supprimé: id={}", id);
    }
}
