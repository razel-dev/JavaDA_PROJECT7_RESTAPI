package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implémentation personnalisée de UserDetailsService utilisée par Spring Security
 * pour charger les informations d’un utilisateur lors du processus d’authentification.
 *
 * Responsabilités :
 * - Récupérer un utilisateur depuis la couche de persistance via UserRepository.
 * - Transformer l’entité métier User en un userdetails.User
 *   exploitable par Spring Security (username, mot de passe encodé, rôles).
 *
 * Comportement:
 * - Si aucun utilisateur n’est trouvé pour le nom d’utilisateur fourni, une UsernameNotFoundException est levée.
 * - Les rôles sont définis avec la méthode {roles(...)} du builder de Spring Security.
 *
 * Remarques:
 * - La méthode {roles(...)} préfixe automatiquement les autorités par {ROLE_}.
 *   Veillez à ne pas stocker le préfixe {ROLE_} dans la colonne role si vous conservez ce mode de construction.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Charge les informations d’un utilisateur à partir de son nom d’utilisateur.
     * Récupère l’entité User depuis le dépôt, puis la convertit en un
     * userdetails.User compatible avec Spring Security,
     * en incluant le nom d’utilisateur, le mot de passe (déjà encodé) et le(s) rôle(s).
     *
     * @param username le nom d’utilisateur dont il faut charger les informations.
     * @return un {UserDetails} contenant les informations de sécurité de l’utilisateur.
     * @throws UsernameNotFoundException si aucun utilisateur n’est trouvé pour le nom fourni.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRole())
                .build();
    }
}