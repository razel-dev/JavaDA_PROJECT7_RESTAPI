package com.nnk.springboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingDto {
    private Integer id;

    @NotBlank(message = "La note Moody's est obligatoire.")
    private String moodysRating;

    @NotBlank(message = "La note S&P est obligatoire.")
    private String sandpRating;

    @NotBlank(message = "La note Fitch est obligatoire.")
    private String fitchRating;

    @Min(value = 0, message = "L'ordre doit être positif ou nul.")
    private Integer orderNumber;
}