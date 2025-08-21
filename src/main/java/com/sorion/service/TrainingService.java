package com.sorion.service;

import com.sorion.domain.*;
import com.sorion.dto.TrainingSessionAnswerRequest;
import com.sorion.dto.TrainingSessionResponse;
import com.sorion.dto.TrainingStepsResponse;
import com.sorion.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingStepRepository trainingStepRepository;
    private final UserRepository userRepository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionAnswerRepository trainingSessionAnswerRepository;


    public List<TrainingStepsResponse> getTrainingSteps(long trainingId) {
        Training training = trainingRepository
                .findById(trainingId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 training 입니다."));

        List<TrainingStepsResponse> responses =
                trainingStepRepository.findByTraining(training)
                        .stream()
                        .map(TrainingStepsResponse::new) // 생성자 기반 변환
                        .toList();

        return responses;
    }

    public TrainingSessionResponse createSession(long userId, long trainingId) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 user 입니다."));
        Training training = trainingRepository.findById(trainingId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 training 입니다."));
        TrainingSession session = new TrainingSession(users, training, LocalDateTime.now());
        trainingSessionRepository.save(session);

        return new TrainingSessionResponse(session.getId(), trainingId, LocalDateTime.now());
    }

    public void saveSessionResult(long sessionId, TrainingSessionAnswerRequest request) {
        TrainingSession trainingSession = trainingSessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 session 입니다."));

        TrainingStep trainingStep = trainingStepRepository.findById(request.stepId()).orElseThrow(() -> new NoSuchElementException("don't exist step"));

        trainingSessionAnswerRepository
                .save(new TrainingSessionAnswer(trainingSession, trainingStep, request.choiceId(), request.isCorrect()));
    }

}
