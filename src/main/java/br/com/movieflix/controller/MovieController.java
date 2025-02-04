package br.com.movieflix.controller;

import br.com.movieflix.controller.request.MovieRequest;
import br.com.movieflix.controller.response.MovieResponse;
import br.com.movieflix.mapper.MovieMapper;
import br.com.movieflix.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/movieflix/movie")
public class MovieController {

    private final MovieService movieService;
    private final PagedResourcesAssembler<MovieResponse> pagedResourcesAssembler;


    public MovieController(MovieService movieService, PagedResourcesAssembler<MovieResponse> pagedResourcesAssembler) {
        this.movieService = movieService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;

    }

    @PostMapping
    public ResponseEntity<MovieResponse> save(@Valid @RequestBody MovieRequest movieRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MovieMapper.toMovieResponse(movieService.save(MovieMapper.toMovie(movieRequest))));

    }

    @Operation(summary = "Buscar Filmes", description = "método que busca todos os filmes",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "retorna todos os filmes",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MovieResponse.class)))
    )
    @PageableAsQueryParam
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<MovieResponse>>> findAll(Pageable pageable) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pagedResourcesAssembler
                        .toModel(movieService.findAll(pageable).map(MovieMapper::toMovieResponse)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> findById(@PathVariable Long id) {
        return movieService
                .findById(id)
                .map(movie -> ResponseEntity.status(HttpStatus.OK)
                        .body(MovieMapper.toMovieResponse(movie)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> update(@PathVariable Long id, @Valid @RequestBody MovieRequest movieRequest) {
        return movieService.update(id, MovieMapper.toMovie(movieRequest))
                .map(movie -> ResponseEntity.status(HttpStatus.OK)
                        .body(MovieMapper.toMovieResponse(movie)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<MovieResponse>> findMovieByCategory(@RequestParam(value = "category",
            defaultValue = "all") String category) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(movieService.findMoviesByCategory(category)
                        .stream()
                        .map(MovieMapper::toMovieResponse).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.findById(id).ifPresent(movie -> movieService.delete(movie.getId()));
        return ResponseEntity.noContent().build();
    }
}
