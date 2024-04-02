package com.specificgroup.emotionstracker.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String login;
    private String token;

}
