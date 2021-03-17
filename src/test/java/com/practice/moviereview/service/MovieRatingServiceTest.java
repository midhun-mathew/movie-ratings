package com.practice.moviereview.service;

import com.practice.moviereview.entity.Movie;
import com.practice.moviereview.entity.User;
import com.practice.moviereview.entity.UserLevel;
import com.practice.moviereview.exceptions.BadRequestException;
import com.practice.moviereview.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieRatingServiceTest {

    @Autowired
    private MovieRatingService movieRatingService;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testComplete(){
        String action = "Action";
        String comedy = "Comedy";
        String drama = "Drama";
        String romance = "Romance";

        UserLevel viewer = movieRatingService.addUserLevel("Viewer", 0, 1);
        UserLevel critic = movieRatingService.addUserLevel("Critic", 3, 2);
        UserLevel expert = movieRatingService.addUserLevel("Expert", 10, 4);
        UserLevel admin = movieRatingService.addUserLevel("Admin", 25, 8);

        movieRatingService.addGenre(action);
        movieRatingService.addGenre(comedy);
        movieRatingService.addGenre(drama);
        movieRatingService.addGenre(romance);

        Movie don = movieRatingService.addMovie("Don", null, LocalDate.of(2006, 1,1), Arrays.asList(action, comedy));
        Movie tiger = movieRatingService.addMovie("Tiger", null, LocalDate.of(2008, 1,1), Arrays.asList(action, comedy));
        Movie padmavat = movieRatingService.addMovie("Padmavat", null, LocalDate.of(2006, 1,1), Arrays.asList(action, comedy));
        Movie lunchbox = movieRatingService.addMovie("Lunchbox", null, LocalDate.of(2022, 1,1), Arrays.asList(action, comedy));
        Movie guru = movieRatingService.addMovie("Guru", null, LocalDate.of(2006, 1,1), Arrays.asList(action, comedy));
        Movie metro = movieRatingService.addMovie("Metro", null, LocalDate.of(2006, 1,1), Arrays.asList(action, comedy));

        User srk = movieRatingService.addUser("SRK");
        User salman = movieRatingService.addUser("Salman");
        User deepika = movieRatingService.addUser("Deepika");

        assertDoesNotThrow(() -> {movieRatingService.reviewMovie(srk.getId(), don.getId(), 2);});
        assertDoesNotThrow(() -> {movieRatingService.reviewMovie(srk.getId(), padmavat.getId(), 8);});
        assertDoesNotThrow(() -> {movieRatingService.reviewMovie(salman.getId(), don.getId(), 5);});
        assertDoesNotThrow(() -> {movieRatingService.reviewMovie(deepika.getId(), don.getId(), 9);});
        assertDoesNotThrow(() -> {movieRatingService.reviewMovie(deepika.getId(), guru.getId(), 6);});
        //Multiple review of same movie
        assertThrows(BadRequestException.class, () -> {movieRatingService.reviewMovie(srk.getId(), don.getId(), 10);});
        // lunchbox not released yet
        assertThrows(BadRequestException.class, () -> {movieRatingService.reviewMovie(srk.getId(), lunchbox.getId(), 5);});
        assertDoesNotThrow(() -> {movieRatingService.reviewMovie(srk.getId(), tiger.getId(), 5);});
        assertDoesNotThrow(() -> {movieRatingService.reviewMovie(srk.getId(), metro.getId(), 7);});
        assertDoesNotThrow(() -> {movieRatingService.reviewMovie(salman.getId(), metro.getId(), 4);});


        List<Movie> movies = movieRatingService.findMovieAverageRatingForYear(LocalDate.of(2006,1,1));
        assertEquals(4, movies.size());

        Movie movie = movieRatingService.findMovieRatingById(metro.getId());
        //(Rating 7 by critic) * 2 & rating 4 by Viewer = (7 + 7 + 4)/3 = 6
        assertEquals(6, movie.getAverageRating());

        List<Movie> topMoviesByCritic = movieRatingService.getTopMovies(10, action, "Critic");
        assertEquals(1, topMoviesByCritic.size());

        List<Movie> topMoviesByViewer = movieRatingService.getTopMovies(10, action, "Viewer");
        assertEquals(5, topMoviesByViewer.size());

    }

}