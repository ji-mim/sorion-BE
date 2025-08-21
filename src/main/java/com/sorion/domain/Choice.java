package com.sorion.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Choice {
    private String id;    // "a", "b" ...
    private String label; // "자동차 경적" ...
}
