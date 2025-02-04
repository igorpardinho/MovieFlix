package br.com.movieflix.controller.request;

import jakarta.validation.constraints.NotBlank;

public record StreamingRequest(@NotBlank(message = "nome obrigatório") String name) {
}
