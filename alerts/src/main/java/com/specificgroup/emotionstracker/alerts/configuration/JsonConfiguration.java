package com.specificgroup.emotionstracker.alerts.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfiguration {

    public ObjectMapper objectMapper(ObjectMapper objectMapper) {
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
}
