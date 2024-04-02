package com.specificgroup.emotionstracker.entries.entry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ErrorDto {
    private String message;
}
