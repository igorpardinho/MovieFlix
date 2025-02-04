package br.com.movieflix.mapper;

import br.com.movieflix.controller.request.MovieRequest;
import br.com.movieflix.controller.response.MovieResponse;
import br.com.movieflix.entity.Category;
import br.com.movieflix.entity.Movie;
import br.com.movieflix.entity.Streaming;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MovieMapper {

    public static Movie toMovie(MovieRequest movieRequest) {


        return Movie.builder()
                .title(movieRequest.title())
                .description(movieRequest.description())
                .releaseDate(movieRequest.releaseDate())
                .rating(movieRequest.rating())
                .categories(movieRequest.categories()
                        .stream()
                        .map(categoryId -> Category.builder().id(categoryId).build())
                        .toList())
                .streamings(movieRequest.streamings()
                        .stream()
                        .map(streamingId -> Streaming.builder().id(streamingId).build())
                        .toList())
                .build();
    }

    public static MovieResponse toMovieResponse(Movie movie) {
        return MovieResponse
                .builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .releaseDate(movie.getReleaseDate())
                .rating(movie.getRating())
                .categories(movie.getCategories().stream().map(CategoryMapper::toCategoryResponse).toList())
                .streamings(movie.getStreamings().stream().map(StreamingMapper::toStreamingResponse).toList())
                .build();
    }
}
