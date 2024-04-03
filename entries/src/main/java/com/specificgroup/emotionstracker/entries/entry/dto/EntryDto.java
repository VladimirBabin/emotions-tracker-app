package com.specificgroup.emotionstracker.entries.entry.dto;

import com.specificgroup.emotionstracker.entries.entry.domain.Emotion;
import com.specificgroup.emotionstracker.entries.entry.domain.State;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Entry log coming from a user
 */
@Value
public class EntryDto {
    @NotNull
    String userId;
    @NotNull
    State state;
    Set<Emotion> emotions;
    String comment;
    LocalDateTime dateTime;
}
