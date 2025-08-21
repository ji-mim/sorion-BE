package com.sorion.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class TrainingSessionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_session_id")
    private TrainingSession trainingSession;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_step_id")
    private TrainingStep trainingStep;

    private boolean correct;
}
