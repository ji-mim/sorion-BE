package com.sorion.repository;

import com.sorion.domain.TrainingStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingStepRepository extends JpaRepository<TrainingStep, Long> {
}
