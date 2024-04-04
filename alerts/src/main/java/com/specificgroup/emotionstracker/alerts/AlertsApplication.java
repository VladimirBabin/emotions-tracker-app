package com.specificgroup.emotionstracker.alerts;

import com.specificgroup.emotionstracker.alerts.configuration.RsaPrivateKey;
import com.specificgroup.emotionstracker.alerts.configuration.RsaPublicKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({RsaPublicKey.class, RsaPrivateKey.class})
@SpringBootApplication
public class AlertsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertsApplication.class, args);
	}

}
