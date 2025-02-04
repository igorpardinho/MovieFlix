package br.com.movieflix.controller.request;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "nome obrigatório")
        String name,
        @NotBlank(message = "email obrigatório")
        String email,
        @NotBlank(message = "senha obrigatória")
        String password) {
}
