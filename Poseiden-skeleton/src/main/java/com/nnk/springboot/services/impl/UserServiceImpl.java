package com.nnk.springboot.services.impl;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.mapper.UserMapper;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implémentation de l'interface UserService pour la gestion des opérations liées aux utilisateurs.
 * Cette classe fournit des méthodes pour créer, lire, mettre à jour, supprimer, gérer les rôles
 * et enregistrer des utilisateurs. Toutes les opérations sont journalisées pour assurer la traçabilité.
 * Elle utilise UserRepository pour les interactions avec la base de données, PasswordEncoder
 * pour le hachage des mots de passe, et UserMapper pour la conversion entre entités et DTO.
 *
 * Les annotations utilisées sont :
 * - @Service pour déclarer un composant de service Spring,
 * - @Slf4j pour la journalisation,
 * - @RequiredArgsConstructor pour l'injection des dépendances via constructeur,
 * - @Transactional pour garantir l'intégrité transactionnelle lors de l'exécution des méthodes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Récupère tous les utilisateurs depuis le dépôt, les mappe en DTO et retourne la liste.
     * L'opération est en lecture seule et journalise le début et la fin avec le temps d'exécution.
     *
     * @return la liste de tous les utilisateurs sous forme de  UserDto
     */
    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        long t0 = System.nanoTime();
        log.info("Liste des utilisateurs - démarrage");
        List<UserDto> result = userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
        log.info("Liste récupérée: {} utilisateur(s) en {} ms", result.size(), (System.nanoTime() - t0) / 1_000_000);
        return result;
    }

    /**
     * Crée un nouvel utilisateur à partir du UserDto fourni et l'enregistre dans le dépôt.
     * Le mot de passe est encodé s'il est présent et non vide. L'identifiant est forcé à null
     * afin d'assurer la création d'un nouvel enregistrement. L'opération est journalisée.
     *
     * @param dto le UserDto contenant les informations de l'utilisateur (username, password, fullname, role)
     * @return l'utilisateur créé en UserDto, avec l'identifiant généré et les autres informations
     */
    @Override
    public UserDto create(UserDto dto) {
        log.info("Création d'un utilisateur: username='{}' role='{}'", dto.getUsername(), dto.getRole());
        dto.setId(null);
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        var entity = userMapper.toEntity(dto);
        var saved = userRepository.save(entity);
        log.info("Utilisateur créé avec succès: id={} username='{}'", saved.getId(), saved.getUsername());
        return userMapper.toDto(saved);
    }

    /**
     * Met à jour les informations d'un utilisateur existant à partir de l'identifiant et des données du {@link UserDto}.
     * Si un mot de passe non vide est fourni, il est encodé avant la mise à jour de l'entité.
     * L'opération est journalisée et lève une exception si l'identifiant n'existe pas dans le dépôt.
     *
     * @param id l'identifiant de l'utilisateur à mettre à jour
     * @param dto le UserDto portant les informations mises à jour (username, password, fullname, role)
     * @return l'utilisateur mis à jour sous forme de UserDto
     * @throws IllegalArgumentException si aucun utilisateur n'est trouvé pour l'identifiant donné
     */
    @Override
    public UserDto update(Integer id, UserDto dto) {
        log.info("Mise à jour de l'utilisateur id={} (username='{}' role='{}')", id, dto.getUsername(), dto.getRole());
        var entity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Mise à jour impossible: utilisateur introuvable pour id={}", id);
                    return new IllegalArgumentException("Utilisateur introuvable avec l'id " + id);
                });


        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userMapper.updateEntity(entity, dto);
        var saved = userRepository.save(entity);
        log.info("Utilisateur mis à jour: id={} username='{}'", saved.getId(), saved.getUsername());
        return userMapper.toDto(saved);
    }

    /**
     * Supprime l'utilisateur correspondant à l'identifiant fourni.
     * Si aucun utilisateur n'est trouvé, une IllegalArgumentException est levée.
     * L'opération de suppression est journalisée, ainsi que l'absence éventuelle de l'utilisateur.
     *
     * @param id l'identifiant de l'utilisateur à supprimer
     * @throws IllegalArgumentException si l'utilisateur n'existe pas pour l'identifiant fourni
     */
    @Override
    public void delete(Integer id) {
        log.info("Suppression de l'utilisateur id={}", id);
        var entity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Suppression impossible: utilisateur introuvable pour id={}", id);
                    return new IllegalArgumentException("Utilisateur introuvable avec l'id " + id);
                });
        userRepository.delete(entity);
        log.info("Utilisateur supprimé: id={}", id);
    }

    /**
     * Récupère un utilisateur par son identifiant unique.
     * L'utilisateur est chargé depuis le dépôt, converti en DTO, puis retourné.
     * Si aucun utilisateur n'est trouvé, une exception est levée. Opération en lecture seule.
     *
     * @param id l'identifiant unique de l'utilisateur à récupérer
     * @return les informations de l'utilisateur en UserDto
     * @throws IllegalArgumentException si aucun utilisateur n'est trouvé pour l'identifiant fourni
     */
    @Transactional(readOnly = true)
    @Override
    public UserDto getUser(Integer id) {
        log.debug("Récupération de l'utilisateur id={}", id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Utilisateur introuvable pour id={}", id);
                    return new IllegalArgumentException("Utilisateur introuvable avec l'id " + id);
                });
        return userMapper.toDto(user);
    }

    /**
     * Enregistre un nouvel utilisateur en construisant un UserDto avec les informations fournies,
     * en encodant le mot de passe, puis en sauvegardant l'utilisateur en base.
     * Les détails d'enregistrement et le succès de l'opération sont journalisés.
     *
     * @param username le nom d'utilisateur du nouvel inscrit
     * @param fullname le nom complet du nouvel inscrit
     * @param password le mot de passe en clair du nouvel inscrit (il sera encodé)
     * @return l'utilisateur enregistré sous forme de UserDto avec l'identifiant généré et les autres informations
     */
    @Override
    public UserDto register(String username, String fullname, String password) {
        log.info("Enregistrement d'un utilisateur: username='{}' fullname='{}'", username, fullname);
        var dto = new UserDto();
        dto.setId(null);
        dto.setUsername(username);
        dto.setFullname(fullname);
        dto.setPassword(passwordEncoder.encode(password));


        var saved = userRepository.save(userMapper.toEntity(dto));
        log.info("Utilisateur enregistré: id={} username='{}'", saved.getId(), saved.getUsername());
        return userMapper.toDto(saved);
    }

    /**
     * Modifie le rôle d'un utilisateur en mettant à jour sa valeur en base.
     * Si l'utilisateur n'existe pas pour l'identifiant fourni, une IllegalArgumentException est levée.
     * Retourne le DTO mis à jour après sauvegarde. L'opération est journalisée.
     *
     * @param id l'identifiant unique de l'utilisateur dont le rôle doit être modifié
     * @param role le nouveau rôle à attribuer à l'utilisateur
     * @return l'utilisateur mis à jour en UserDto avec le nouveau rôle
     * @throws IllegalArgumentException si aucun utilisateur n'est trouvé pour l'identifiant fourni
     */
    @Override
    public UserDto changeUserRole(Integer id, String role) {
        log.info("Changement de rôle utilisateur id={} -> role='{}'", id, role);
        var entity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Changement de rôle impossible: utilisateur introuvable pour id={}", id);
                    return new IllegalArgumentException("Utilisateur introuvable avec l'id " + id);
                });
        entity.setRole(role);
        var saved = userRepository.save(entity);
        log.info("Rôle utilisateur mis à jour: id={} role='{}'", saved.getId(), role);
        return userMapper.toDto(saved);
    }
}