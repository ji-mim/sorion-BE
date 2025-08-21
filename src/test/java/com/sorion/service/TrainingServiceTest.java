package com.sorion.service;

import com.sorion.domain.*;
import com.sorion.dto.TrainingSessionAnswerRequest;
import com.sorion.dto.TrainingSessionResponse;
import com.sorion.dto.TrainingStepsResponse;
import com.sorion.repository.*;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = ALL)
class TrainingServiceTest {

    private final TrainingService trainingService;
    private final TrainingRepository trainingRepository;
    private final TrainingStepRepository trainingStepRepository;
    private final UserRepository userRepository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionAnswerRepository trainingSessionAnswerRepository;
    private final CategoryRepository categoryRepository;
    private final SoundRepository soundRepository;

    @Test
    void getTrainingSteps() {
        //given
        Category category = categoryRepository.save(new Category("일상", "example.con"));
        Training training = trainingRepository.save(new Training(category, "exTrain"));
        Sound sound1 = soundRepository.save(new Sound("헤어드라이기", "exam.com", category, 10));
        Sound sound2 = soundRepository.save(new Sound("선풍기", "exam.com", category, 10));
        List<Choice> choices = List.of(
                Choice.builder().id("a").label("자동차 경적").build(),
                Choice.builder().id("b").label("휘파람").build(),
                Choice.builder().id("c").label("폭죽").build(),
                Choice.builder().id("d").label("유리").build()
        );
        trainingStepRepository.save(new TrainingStep(training, sound1, 1, choices, "a"));
        trainingStepRepository.save(new TrainingStep(training, sound2, 1, choices, "a"));

        //when
        List<TrainingStepsResponse> trainingSteps = trainingService.getTrainingSteps(training.getId());

        //then
        assertThat(trainingSteps.size()).isEqualTo(2);
    }

    @Test
    void createSession() {
        //given
        Category category = categoryRepository.save(new Category("일상", "example.con"));
        Users user = userRepository.save(new Users("user1"));
        Training training = trainingRepository.save(new Training(category, "exTrain"));

        //when
        trainingService.createSession(user.getId(), training.getId());

        //then
        List<TrainingSession> collect = trainingSessionRepository.findAll().stream().toList();
        assertThat(collect.size()).isEqualTo(1);
    }

    @Test
    void saveSessionResult() {
        //given
        Category category = categoryRepository.save(new Category("일상", "example.con"));
        Training training = trainingRepository.save(new Training(category, "exTrain"));
        Sound sound2 = soundRepository.save(new Sound("선풍기", "exam.com", category, 10));
        List<Choice> choices = List.of(
                Choice.builder().id("a").label("자동차 경적").build(),
                Choice.builder().id("b").label("휘파람").build(),
                Choice.builder().id("c").label("폭죽").build(),
                Choice.builder().id("d").label("유리").build()
        );
        TrainingStep trainingStep1 = trainingStepRepository.save(new TrainingStep(training, sound2, 1, choices, "a"));
        Users user = userRepository.save(new Users("user1"));
        TrainingSession session = trainingSessionRepository.save(new TrainingSession(user, training, LocalDateTime.now()));
        TrainingSessionAnswerRequest request = new TrainingSessionAnswerRequest(trainingStep1.getId(), "a", true);

        //when
        trainingService.saveSessionResult(session.getId(), request);

        //then
        List<TrainingSessionAnswer> result = trainingSessionAnswerRepository.findAll().stream().toList();
        assertThat(result.size()).isEqualTo(1);
    }
}