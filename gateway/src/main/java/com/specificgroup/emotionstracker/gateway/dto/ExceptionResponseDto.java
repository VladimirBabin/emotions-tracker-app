package com.specificgroup.emotionstracker.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponseDto {

    private String errCode;
    private String err;
    private String errDetails;
    private LocalDateTime localDateTime;

}

