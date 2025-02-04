package br.com.movieflix.controller.response;

import lombok.*;

@Builder
public record CategoryResponse(Long id,String name) {
}
