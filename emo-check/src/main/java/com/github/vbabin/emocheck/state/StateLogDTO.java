package com.github.vbabin.emocheck.state;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class StateLogDTO {
    @NotBlank
    String userAlias;
    @NotNull
    State state;
    LocalDateTime dateTime;
}
