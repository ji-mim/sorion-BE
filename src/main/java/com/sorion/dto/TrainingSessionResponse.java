package com.sorion.dto;

import java.time.LocalDateTime;

/**
 * { "session_id":"ses_9abC",
 * ”training_id”:trn_quiet_room”,
 * "started_at":"..." }
 */
public record TrainingSessionResponse(
        long sessionId,
        long trainingId,
        LocalDateTime startedAt
) {
}
