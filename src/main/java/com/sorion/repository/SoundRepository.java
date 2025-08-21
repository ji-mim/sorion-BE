package com.sorion.repository;

import com.sorion.domain.Category;
import com.sorion.domain.Sound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SoundRepository extends JpaRepository<Sound, Long> {
    List<Sound> findByCategory(Category category);
    Optional<Sound> findBySoundNameAndCategory(String soundName, Category category);

}
