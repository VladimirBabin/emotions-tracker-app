package com.specificgroup.emotionstracker.entries;

import com.specificgroup.emotionstracker.entries.configuration.RsaKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKey.class)
@SpringBootApplication
public class EntriesApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntriesApplication.class, args);
    }

}
