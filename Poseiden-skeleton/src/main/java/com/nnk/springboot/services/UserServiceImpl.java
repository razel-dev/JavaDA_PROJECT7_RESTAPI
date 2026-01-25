package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.mapper.UserMapper;
import com.nnk.springboot.repositories.UserRepository;

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
    public List<User> findAll ()
    {
        return userRepository.findAll();
    }

    @Override
    public User create(UserDto dto) {
        // Unicité du username
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà utilisé.");
        }
        // Utilisation du mapper MapStruct
        User entity = userMapper.toEntity(dto);
        // Encodage du mot de passe
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(entity);
    }

    @Override
    public User update(Integer id, UserDto dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable pour id: " + id));

        // Unicité username (existe-t-il un utilisateur avec ce username et un id différent ?)
        if (userRepository.existsByUsernameAndIdNot(dto.getUsername(), id)) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà utilisé.");
        }

        // Met à jour l'entité existante via MapStruct
        userMapper.updateEntity(user, dto);
        // Ré-encodage du mot de passe uniquement si fourni (non null et non vide)
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable pour id: " + id));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getDto(Integer id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable pour id: " + id));
        UserDto dto = userMapper.toDto(user);

        // Optionnel: forcer le mot de passe vide dans le DTO
        dto.setPassword("");
        return dto;
    }
}