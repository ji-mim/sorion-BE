package com.sorion.dto;


import java.util.List;

public record SoundResponse(
        long categoryId,
        int count,
        List<SoundList> soundList
) {
}
