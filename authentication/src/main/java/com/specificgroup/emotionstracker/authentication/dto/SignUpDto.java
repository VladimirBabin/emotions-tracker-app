package com.specificgroup.emotionstracker.authentication.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignUpDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String login;
    @NotNull
    private char[] password;
}
