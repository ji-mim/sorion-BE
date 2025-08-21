package com.sorion.dto;


/**
 * "step_id":"stp_001",
 * "step_order":1,
 * "choice_id":"b",
 * "correct":true
 */
public record TrainingSessionAnswerRequest(
        long stepId,
        String choiceId,
        boolean isCorrect
) {
}
