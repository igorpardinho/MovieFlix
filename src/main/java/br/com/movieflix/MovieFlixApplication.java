package br.com.movieflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MovieFlixApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieFlixApplication.class, args);
    }

}
