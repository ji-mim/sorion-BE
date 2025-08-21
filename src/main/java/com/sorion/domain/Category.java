package com.sorion.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    private String imagePath;

    protected Category() {
    }

    public Category(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }
}
