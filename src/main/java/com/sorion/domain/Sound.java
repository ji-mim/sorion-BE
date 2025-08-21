package com.sorion.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Sound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sound_id")
    private Long id;
    private String soundName;
    private String soundPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    int duration;

    int soundOrder;


    protected Sound() {
    }
}
