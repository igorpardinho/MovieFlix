package br.com.movieflix.controller.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;


public record MovieRequest(@NotBlank(message = "titulo obrigatório")
                           String title,

                           String description,

                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                           LocalDate releaseDate,

                           double rating,
                           List<Long> categories,
                           List<Long> streamings

) {
}
