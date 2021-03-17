package com.practice.moviereview.service;

import com.practice.moviereview.entity.*;
import com.practice.moviereview.exceptions.BadRequestException;
import com.practice.moviereview.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieRatingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRatingRepository userRatingRepository;

    @Autowired
    private UserLevelRepository userLevelRepository;

    @Autowired
    private GenreRepository genreRepository;


    public User addUser(String userName){
        UserLevel userLevel = userLevelRepository.getDefaultUserLevel();

        User user = User.builder()
                .userRatings(new HashSet<>())
                .name(userName)
                .build();
        return userRepository.save(user);
    }

    public Movie addMovie(String name, String description, LocalDate releaseDate, List<String> genreNames){
        List<Genre> genres = genreRepository.findByNames(genreNames);
        Movie movie = Movie.builder()
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .averageRating(null)
                .userRatings(new HashSet<>())
                .genre(genres)
                .build();
        return movieRepository.save(movie);
    }


    public UserRating reviewMovie(int userId, int movieId, int rating) throws BadRequestException{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("No user with given id found"));
        Movie movie = movieRepository.findByIdIfReleased(movieId)
                .orElseThrow(() -> new BadRequestException("No released movie with given id found"));

        UserRating userRating = userRatingRepository.getUserRatingForMovie(userId, movieId);
        if(userRating != null){
            throw new BadRequestException("user has already rated this movie");
        }
        UserLevel userLevel = userLevelRepository.calculateUserLevel(userId);
        userRating = userRating.builder()
                .rating(rating)
                .user(user)
                .movie(movie)
                .level(userLevel)
                .build();
        userRating = userRatingRepository.save(userRating);

        Double newMoviewRating = userRatingRepository.calculateNewRatingOfMovie(movieId);
        movieRepository.updateMovieRating(movieId, newMoviewRating);
        return userRating;
    }

    public List<Movie> getTopMovies(int limit, String genre, String userLevel){
        List<Movie> movies = movieRepository.findTopMovies(genre, userLevel);
        return movies.stream().limit(limit).collect(Collectors.toList());
    }

    public List<Movie> findMovieAverageRatingForYear(LocalDate date){
        return movieRepository.findMovieByYear(date);
    }

    public Movie findMovieRatingById(Integer movieId){
        return movieRepository.findById(movieId).orElse(null);
    }

    public void addGenre(String name){
        Genre genre = Genre.builder()
                .name(name)
                .build();
        genreRepository.save(genre);
    }

    public UserLevel addUserLevel(String name, Integer minReviewCount, Integer ratingWeight){
        UserLevel userLevel = UserLevel.builder()
                .name(name)
                .minReviewCount(minReviewCount)
                .ratingWeight(ratingWeight)
                .build();
        return userLevelRepository.save(userLevel);
    }


}
