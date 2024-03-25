package com.specificgroup.emotionstracker.entrylogging.configuration;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.databind.Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfiguration {

    @Bean
    public Module  hibernateModule() {
        return new Hibernate6Module();
    }
}
