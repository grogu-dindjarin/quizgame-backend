package com.bramengel.quizgame.repository;

import com.bramengel.quizgame.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Optional<Badge> findByCondition(String condition);
}
