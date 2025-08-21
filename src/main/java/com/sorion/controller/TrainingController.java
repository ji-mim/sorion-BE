package com.sorion.controller;

import com.sorion.dto.TrainingSessionRequest;
import com.sorion.dto.TrainingSessionAnswerRequest;
import com.sorion.dto.TrainingSessionResponse;
import com.sorion.dto.TrainingStepsResponse;
import com.sorion.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingService trainingService;

    @GetMapping("/{trainingId}")
    public ResponseEntity<List<TrainingStepsResponse>> getTrainingSteps(@PathVariable Long trainingId) {
        return new ResponseEntity<>(trainingService.getTrainingSteps(trainingId), HttpStatus.OK);
    }

    @PostMapping("/sessions")
    public ResponseEntity<TrainingSessionResponse> createTrainingSession(@RequestBody TrainingSessionRequest request) {
        return new ResponseEntity<>(trainingService.createSession(request.userId(), request.trainingId()), HttpStatus.CREATED);
    }

    @PostMapping("/sessions/{sessionId}/answer")
    public ResponseEntity<Void> createTrainingSessionAnswer(
            @PathVariable Long sessionId,
            @RequestBody TrainingSessionAnswerRequest request) {

        trainingService.saveSessionResult(sessionId, request);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
