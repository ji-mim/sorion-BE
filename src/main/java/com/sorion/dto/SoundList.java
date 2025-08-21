package com.sorion.dto;

import com.sorion.domain.Sound;

/**
 * "sound_id": "snd_dryer",
 * "name": "헤어 드라이어",
 * "file_url": "https://cdn.example.com/mp3,
 * "duration_sec": 10,
 */
public record SoundList(
        long soundId,
        String name,
        String fileUrl,
        int durationSec
) {

    public SoundList(Sound sound) {
        this(
                sound.getId(),
                sound.getSoundName(),
                sound.getSoundPath(),
                sound.getDuration()
        );
    }
}
