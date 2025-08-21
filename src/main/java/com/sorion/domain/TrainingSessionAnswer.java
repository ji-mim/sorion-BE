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

    private String choice;

    private boolean isCorrect;

    protected TrainingSessionAnswer() {
    }

    public TrainingSessionAnswer(TrainingSession trainingSession, TrainingStep trainingStep, String choice, boolean isCorrect) {
        this.trainingSession = trainingSession;
        this.trainingStep = trainingStep;
        this.choice = choice;
        this.isCorrect = isCorrect;
    }
}
