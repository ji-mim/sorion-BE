package com.sorion.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class TrainingStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_step_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id")
    private Training training;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sound_id")
    private Sound sound;

    private int stepOrder;

    @Convert(converter = ChoiceListConverter.class)
    @Column(columnDefinition = "json")
    private List<Choice> choices = new ArrayList<>();

    private String answer;

    protected TrainingStep() {
    }
}
