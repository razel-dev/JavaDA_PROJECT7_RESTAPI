package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RuleNameDto {
    private Integer id;

    @NotBlank(message = "Le nom est obligatoire.")
    private String name;

    @NotBlank(message = "La description est obligatoire.")
    private String description;


    private String json;

    private String template;

    private String sqlStr;

    private String sqlPart;
}