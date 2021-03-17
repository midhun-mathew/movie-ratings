package com.practice.moviereview.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;
    private String description;
    private Double averageRating;
    private LocalDate releaseDate;
    @ManyToMany
    private List<Genre> genre;


    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private Set<UserRating> userRatings;

    public Movie(Integer id, String name, String description, Double averageRating, LocalDate releaseDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.averageRating = averageRating;
        this.releaseDate = releaseDate;
    }
}
