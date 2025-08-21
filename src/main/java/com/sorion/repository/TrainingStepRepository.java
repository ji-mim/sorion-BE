package com.sorion.repository;

import com.sorion.domain.Training;
import com.sorion.domain.TrainingStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingStepRepository extends JpaRepository<TrainingStep, Long> {
    List<TrainingStep> findByTraining(Training training);
}
