package com.specificgroup.emotionstracker.entrylogging;

import com.specificgroup.emotionstracker.entrylogging.configuration.RsaKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKey.class)
@SpringBootApplication
public class EntryLoggingApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntryLoggingApplication.class, args);
    }

}
