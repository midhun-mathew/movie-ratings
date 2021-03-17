package com.practice.moviereview.repository;

import com.practice.moviereview.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    @Query("select m from Movie m where releaseDate <= CURRENT_DATE and m.id = :id")
    Optional<Movie> findByIdIfReleased(Integer id);

    @Modifying
    @Query("update Movie set averageRating = :rating where id = :movieId")
    void updateMovieRating(Integer movieId, Double rating);

    @Query("select new Movie(m.id, m.name, m.description, cast(sum(ur.rating * l.ratingWeight) as double)/sum(l.ratingWeight) as averageRating, m.releaseDate) " +
            "from Movie m  inner join m.userRatings ur inner join ur.level l inner join m.genre g where l.name = :userLevel and g.name = :genre " +
            "group by m.id order by averageRating desc")
    List<Movie> findTopMovies(String genre, String userLevel);

    @Query("select m from Movie m where year(releaseDate) = year(:year)")
    List<Movie> findMovieByYear(LocalDate year);

}
