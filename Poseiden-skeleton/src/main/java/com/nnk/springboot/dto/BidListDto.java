package com.nnk.springboot.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class BidListDto {
    // id utilisé pour l’update (hidden field dans le formulaire)
    private Integer id;

    @NotBlank(message = "Le compte est obligatoire.")
    private String account;

    @NotBlank(message = "Le type est obligatoire.")
    private String type;

    @DecimalMin(value = "0.0", message = "La quantité d'enchère doit être positive ou nulle.")
    @Digits(integer = 19, fraction = 4, message = "La quantité d'enchère doit avoir au plus 19 chiffres et 4 décimales.")
    private BigDecimal bidQuantity;
}