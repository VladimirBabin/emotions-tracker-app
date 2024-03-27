package com.specificgroup.emotionstracker.entrylogging.state;

import com.specificgroup.emotionstracker.entrylogging.state.domain.Emotion;
import com.specificgroup.emotionstracker.entrylogging.state.domain.State;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * State log coming from a user
 */
@Value
public class EntryLogDTO {
    @NotNull
    String userId;
    @NotNull
    State state;
    Set<Emotion> emotions;
    LocalDateTime dateTime;
}
