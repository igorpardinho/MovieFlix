package br.com.movieflix.service;


import br.com.movieflix.entity.Category;
import br.com.movieflix.entity.Movie;
import br.com.movieflix.entity.Streaming;
import br.com.movieflix.repository.MovieRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final CategoryService categoryService;
    private final StreamingService streamingService;

    public MovieService(MovieRepository movieRepository, CategoryService categoryService, StreamingService streamingService) {
        this.movieRepository = movieRepository;
        this.categoryService = categoryService;
        this.streamingService = streamingService;
    }

    public Movie save(Movie movie) {
        movie.setCategories(this.findCategories(movie.getCategories()));
        movie.setStreamings(this.findStreamings(movie.getStreamings()));
        return movieRepository.save(movie);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "Movie", key = "'all_movies_page_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()")
    public Page<Movie> findAll(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "Movie", key = "#id")
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    @Transactional
    @CacheEvict(value = "Movie", allEntries = true)
    public Optional<Movie> update(Long id, Movie movieUpdated) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            movie.get().setTitle(movieUpdated.getTitle());
            movie.get().setDescription(movieUpdated.getDescription());
            movie.get().setRating(movieUpdated.getRating());
            movie.get().setCategories(movieUpdated.getCategories());

            movie.get().getCategories().clear();
            movie.get().getCategories().addAll(this.findCategories(movieUpdated.getCategories()));

            movie.get().getStreamings().clear();
            movie.get().getStreamings().addAll(this.findStreamings(movieUpdated.getStreamings()));


            return Optional.of(movieRepository.save(movie.get()));
        }
        return Optional.empty();
    }

    @Transactional
    @CacheEvict(value = "Movie", allEntries = true)
    public void delete(Long id) {
        movieRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "Movie")
    public List<Movie> findMoviesByCategory(String categoryName) {
        List<Movie> movies = new ArrayList<>();

        for (Movie movie : movieRepository.findAll()) {
            if (movie.getCategories().stream().anyMatch(c -> c.getName().equals(categoryName))) {
                movies.add(movie);
            }
        }
        return movies;
    }

    private List<Category> findCategories(List<Category> categories) {
        List<Category> categoriesFound = new ArrayList<>();

        categories.forEach(category -> categoryService
                .findById(category.getId()).ifPresent(categoriesFound::add));

        return categoriesFound;
    }

    private List<Streaming> findStreamings(List<Streaming> streamings) {
        List<Streaming> streamingsFound = new ArrayList<>();
        streamings
                .forEach(streaming -> streamingService
                        .findById(streaming.getId()).ifPresent(streamingsFound::add));
        return streamingsFound;
    }
}
