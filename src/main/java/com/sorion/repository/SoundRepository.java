package com.sorion.repository;

import com.sorion.domain.Sound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoundRepository extends JpaRepository<Sound, Long> {
}
