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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

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