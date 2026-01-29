package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.mapper.UserMapper;
import com.nnk.springboot.repositories.UserRepository;

import com.nnk.springboot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto create(UserDto dto) {
        // Unicité du username
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà utilisé.");
        }
        // MapStruct -> entité
        User entity = userMapper.toEntity(dto);
        // Encodage du mot de passe
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(entity);
        UserDto out = userMapper.toDto(saved);

        out.setPassword("");
        return out;
    }

    @Override
    public UserDto update(Integer id, UserDto dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable pour id: " + id));

        // Unicité username
        if (userRepository.existsByUsernameAndIdNot(dto.getUsername(), id)) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà utilisé.");
        }

        // Mise à jour partielle via MapStruct
        userMapper.updateEntity(user, dto);
        // Ré-encodage si un nouveau mot de passe est fourni
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User saved = userRepository.save(user);
        UserDto out = userMapper.toDto(saved);
        out.setPassword("");
        return out;
    }

    @Override
    public void delete(Integer id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable pour id: " + id));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(Integer id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable pour id: " + id));
        UserDto dto = userMapper.toDto(user);

        dto.setPassword("");
        return dto;
    }


    @Override
    public UserDto register(String username, String fullName, String rawPassword) {
        // Unicité du username
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà utilisé.");
        }
        // Création forcée avec rôle USER
        User user = new User();
        user.setUsername(username);
        user.setFullname(fullName);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("ROLE_USER"); // imposé côté serveur

        User saved = userRepository.save(user);
        UserDto dto = userMapper.toDto(saved);
        dto.setPassword("");
        return dto;
    }

    @Override
    public UserDto changeUserRole(Integer id, String role) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable pour id: " + id));

        String normalized = role == null ? "" : role.trim().toUpperCase();
        if (!normalized.equals("USER") && !normalized.equals("ADMIN")) {
            throw new IllegalArgumentException("Rôle invalide (attendu: USER ou ADMIN).");
        }
        user.setRole("ROLE_" + normalized);

        User saved = userRepository.save(user);
        UserDto dto = userMapper.toDto(saved);
        dto.setPassword("");
        return dto;
    }
}