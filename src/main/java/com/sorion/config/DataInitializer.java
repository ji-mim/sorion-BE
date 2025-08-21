// src/main/java/com/sorion/config/DataInitializer.java
package com.sorion.config;

import com.sorion.domain.*;
import com.sorion.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final SoundRepository soundRepository;
    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingStepRepository trainingStepRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // 1) 카테고리/유저 upsert
        Category daily = categoryRepository.findByName("일상")
                .orElseGet(() -> categoryRepository.save(new Category("일상", "daily.png")));

        Users tester = userRepository.findByUsername("tester")
                .orElseGet(() -> userRepository.save(new Users("tester")));

        // 2) 사운드 upsert
        Sound s1 = getOrCreateSound("항공기", "https://cdn.example.com/dryer.mp3", daily, 39);
        Sound s2 = getOrCreateSound("자동차", "https://cdn.example.com/fan.mp3",   daily, 11);
        Sound s3 = getOrCreateSound("고양이", "https://cdn.example.com/fan.mp3",   daily, 31);
        Sound s4 = getOrCreateSound("세탁기", "https://cdn.example.com/fan.mp3",   daily, 22);
        Sound s5 = getOrCreateSound("지하철", "https://cdn.example.com/fan.mp3",   daily, 16);
        Sound s6 = getOrCreateSound("발소리", "https://cdn.example.com/fan.mp3",   daily, 41);

        // 3) 트레이닝 upsert
        Training training = trainingRepository.findByTitleAndCategory("소리 적응 트레이닝", daily)
                .orElseGet(() -> trainingRepository.save(new Training(daily, "소리 적응 트레이닝")));

        // 공통 선택지
        List<Choice> ch1 = List.of(
                Choice.builder().id("a").label("항공기").build(),
                Choice.builder().id("b").label("휘파람").build(),
                Choice.builder().id("c").label("폭죽").build(),
                Choice.builder().id("d").label("유리").build()
        );

        List<Choice> ch2 = List.of(
                Choice.builder().id("a").label("휘파람").build(),
                Choice.builder().id("b").label("자동차").build(),
                Choice.builder().id("c").label("폭죽").build(),
                Choice.builder().id("d").label("유리").build()
        );

        List<Choice> ch3 = List.of(
                Choice.builder().id("a").label("휘파람").build(),
                Choice.builder().id("b").label("자동차").build(),
                Choice.builder().id("c").label("폭죽").build(),
                Choice.builder().id("d").label("유리").build()
        );

        List<Choice> ch4 = List.of(
                Choice.builder().id("a").label("고양이").build(),
                Choice.builder().id("b").label("비행기").build(),
                Choice.builder().id("c").label("빗소리").build(),
                Choice.builder().id("d").label("세탁기").build()
        );

        List<Choice> ch5 = List.of(
                Choice.builder().id("a").label("자동차").build(),
                Choice.builder().id("b").label("고함").build(),
                Choice.builder().id("c").label("휘파람").build(),
                Choice.builder().id("d").label("지하철").build()
        );

        List<Choice> ch6 = List.of(
                Choice.builder().id("a").label("폭죽").build(),
                Choice.builder().id("b").label("항공기").build(),
                Choice.builder().id("c").label("발소리").build(),
                Choice.builder().id("d").label("전철").build()
        );

        // 4) 스텝 upsert (training+sound+stepOrder 유니크하게 간주)
        createStepIfNotExists(training, s1, 1, ch1, "a");
        createStepIfNotExists(training, s2, 2, ch2, "b");
        createStepIfNotExists(training, s3, 3, ch3, "b");
        createStepIfNotExists(training, s4, 4, ch4, "d");
        createStepIfNotExists(training, s5, 5, ch5, "d");
        createStepIfNotExists(training, s6, 6, ch6, "c");

        // TestResult는 “항상 저장”이면 중복될 수 있으니 기본은 생략
        // 필요하면 exists 체크 후 1개만 넣도록 구현하세요.
    }

    private Sound getOrCreateSound(String name, String url, Category category, int duration) {
        return soundRepository.findBySoundNameAndCategory(name, category)
                .orElseGet(() -> soundRepository.save(new Sound(name, url, category, duration)));
    }

    private void createStepIfNotExists(Training training, Sound sound, int order, List<Choice> choices, String answer) {
        boolean exists = trainingStepRepository.existsByTrainingAndSoundAndStepOrder(training, sound, order);
        if (!exists) {
            trainingStepRepository.save(new TrainingStep(training, sound, order, choices, answer));
        }
    }
}