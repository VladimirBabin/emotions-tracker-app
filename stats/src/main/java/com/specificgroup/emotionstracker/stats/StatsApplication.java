package com.specificgroup.emotionstracker.stats;

import com.specificgroup.emotionstracker.stats.configuration.RsaPrivateKey;
import com.specificgroup.emotionstracker.stats.configuration.RsaPublicKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({RsaPrivateKey.class, RsaPublicKey.class})
@SpringBootApplication
public class StatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatsApplication.class, args);
    }

}
