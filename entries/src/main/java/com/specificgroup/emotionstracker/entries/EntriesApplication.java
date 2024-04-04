package com.specificgroup.emotionstracker.entries;

import com.specificgroup.emotionstracker.entries.configuration.RsaPrivateKey;
import com.specificgroup.emotionstracker.entries.configuration.RsaPublicKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({RsaPublicKey.class, RsaPrivateKey.class})
@SpringBootApplication
public class EntriesApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntriesApplication.class, args);
    }

}
