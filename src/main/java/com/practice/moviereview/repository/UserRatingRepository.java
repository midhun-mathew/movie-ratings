package com.practice.moviereview.repository;

import com.practice.moviereview.entity.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, Integer> {
    @Query("select cr from UserRating cr where cr.movie.id = :movieId and cr.user.id = :userId")
    UserRating getUserRatingForMovie(Integer userId, Integer movieId);

    @Query("select cr from UserRating cr where cr.user.id = :userId")
    List<UserRating> findRatingsByUser(Integer userId);

    @Query("select cast(sum(cr.rating * cr.level.ratingWeight) as double)/sum(cr.level.ratingWeight) from UserRating cr where cr.movie.id = :movieId")
    Double calculateNewRatingOfMovie(Integer movieId);



}
