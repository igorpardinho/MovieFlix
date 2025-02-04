package br.com.movieflix.controller.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "email obrigatório")
                           String email,

                           @NotBlank(message = "senha obrigatória")
                           String password) {
}
