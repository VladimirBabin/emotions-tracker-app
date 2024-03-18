package com.specificgroup.emotionstracker.emocheck.state;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * State log coming from a user
 */
@Value
public class StateLogDTO {
    @NotBlank
    String userAlias;
    @NotNull
    State state;
    Set<Emotion> emotions;
    LocalDateTime dateTime;
}
