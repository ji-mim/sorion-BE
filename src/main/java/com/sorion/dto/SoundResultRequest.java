package com.sorion.dto;

public record SoundResultRequest(
    long userId,
    long soundId,
    int sensitivity
) {
}
