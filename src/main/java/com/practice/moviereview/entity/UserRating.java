package com.practice.moviereview.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRating {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @Min(1)
    @Max(10)
    private Integer rating;
    @OneToOne
    private UserLevel level;

}
