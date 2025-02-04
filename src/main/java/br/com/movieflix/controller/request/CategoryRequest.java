package br.com.movieflix.controller.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(@NotBlank(message = "nome obrigatório") String name) {
}
