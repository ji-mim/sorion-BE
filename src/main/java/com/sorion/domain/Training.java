package com.sorion.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    String title;

    protected Training() {
    }
}
