package com.practice.moviereview.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLevel {
    @Id
    @GeneratedValue
    Integer id;
    String name;
    Integer minReviewCount;
    Integer ratingWeight;
}
