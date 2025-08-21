package com.sorion.repository;

import com.sorion.domain.Category;
import com.sorion.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    Optional<Training> findByTitleAndCategory(String title, Category category);

}
