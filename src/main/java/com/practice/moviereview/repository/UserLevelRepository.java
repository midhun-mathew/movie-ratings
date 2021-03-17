package com.practice.moviereview.repository;

import com.practice.moviereview.entity.UserLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLevelRepository extends JpaRepository<UserLevel, Integer> {

    @Query("select c from UserLevel c where c.minReviewCount in (select max(c1.minReviewCount) from UserLevel c1 where c1.minReviewCount <= (select count(ur) from UserRating ur where ur.user.id = :userId))")
    UserLevel calculateUserLevel(Integer userId);

    @Query("select ul from UserLevel ul where minReviewCount = 0")
    UserLevel getDefaultUserLevel();

}
