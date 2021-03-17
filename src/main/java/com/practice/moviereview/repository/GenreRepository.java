package com.practice.moviereview.repository;

import com.practice.moviereview.entity.Genre;
import com.practice.moviereview.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
    @Query("select g from Genre g where g.name in :names")
    public List<Genre> findByNames(List<String> names);
}
