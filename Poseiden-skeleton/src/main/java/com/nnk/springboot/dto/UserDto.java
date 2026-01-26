
package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    public interface Create {}
    public interface Update {}

    private Integer id;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire.")
    @Size(max = 125, message = "Le nom d'utilisateur ne doit pas dépasser 125 caractères.")
    private String username;

    @Size(max = 125, message = "Le mot de passe ne doit pas dépasser 125 caractères.")
    @NotBlank(groups = Create.class, message = "Le mot de passe est obligatoire.")
    private String password;

    @NotBlank(message = "Le nom complet est obligatoire.")
    @Size(max = 125, message = "Le nom complet ne doit pas dépasser 125 caractères.")
    private String fullname;

    @NotBlank(message = "Le rôle est obligatoire.")
    @Size(max = 125, message = "Le rôle ne doit pas dépasser 125 caractères.")
    private String role;
}