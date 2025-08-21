package com.sorion.repository;

import com.sorion.domain.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
}
