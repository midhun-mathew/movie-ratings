package com.practice.moviereview.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;
    @ManyToMany(mappedBy = "genre")
    private List<Movie> movies;

}
