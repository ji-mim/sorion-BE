package com.sorion.service;

import com.sorion.domain.Category;
import com.sorion.domain.Sound;
import com.sorion.domain.TestResult;
import com.sorion.domain.Users;
import com.sorion.dto.SoundList;
import com.sorion.dto.SoundResponse;
import com.sorion.repository.CategoryRepository;
import com.sorion.repository.SoundRepository;
import com.sorion.repository.TestResultRepository;
import com.sorion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SoundTestService {

    private final SoundRepository soundRepository;
    private final CategoryRepository categoryRepository;
    private final TestResultRepository testResultRepository;
    private final UserRepository userRepository;

    public SoundResponse findTestSounds(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다."));
        List<SoundList> soundResponse = soundRepository
                .findByCategory(category)
                .stream()
                .map(SoundList::new)
                .toList();

        return new SoundResponse(categoryId, soundResponse.size(), soundResponse);
    }

    public void testResultSave(long userId, long soundId, int sensitivity) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 user 입니다.."));
        Sound sound = soundRepository.findById(soundId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 sound 입니다."));

        TestResult testResult = new TestResult(users, sound, LocalDateTime.now(), sensitivity);
        testResultRepository.save(testResult);
    }
}
