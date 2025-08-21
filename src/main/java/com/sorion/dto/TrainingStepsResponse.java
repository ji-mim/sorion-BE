package com.sorion.dto;

import com.sorion.domain.Choice;
import com.sorion.domain.TrainingStep;

import java.util.List;

/**
 * {
 * "step_id": "stp_001",
 * "step_order": 1,
 * "sound_name": "dryer",
 * "duration_sec": 10,
 * ”sound_url”: com.exam.www,
 * "choices":[{"id":"a","label":"자동차 경적"}, {"id":"b","label":"휘파람"}, {"id":"c","label":"폭죽"}, {"id":"d","label":"유리"}],
 * "correctChoiceId":"b"}
 */
public record TrainingStepsResponse(
        long stepId,
        int stepOrder,
        String soundName,
        int durationSec,
        String soundUrl,
        List<Choice> choices,
        String correctChoiceId
) {
    public TrainingStepsResponse(TrainingStep trainingStep) {
        this(
                trainingStep.getId(),
                trainingStep.getStepOrder(),
                trainingStep.getSound().getSoundName(),
                trainingStep.getSound().getDuration(),
                trainingStep.getSound().getSoundPath(),
                trainingStep.getChoices(),
                trainingStep.getAnswer()
        );
    }
}