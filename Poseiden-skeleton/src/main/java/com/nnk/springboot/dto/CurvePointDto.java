package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurvePointDto {
    private Integer id;

    @NotNull(message = "L'identifiant de courbe (curveId) est obligatoire.")
    private Integer curveId;


    private LocalDateTime asOfDate;

    @NotNull(message = "Le terme (term) est obligatoire.")
    @PositiveOrZero(message = "Le terme doit être positif ou nul.")
    private Double term;

    @NotNull(message = "La valeur (value) est obligatoire.")
    @PositiveOrZero(message = "La valeur doit être positive ou nulle.")
    private Double value;


    private LocalDateTime creationDate;
}