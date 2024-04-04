package com.specificgroup.emotionstracker.stats.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

import java.security.interfaces.RSAPrivateKey;

@ConfigurationProperties(prefix = "rsa-private")
@Profile("test")
public record RsaPrivateKey(RSAPrivateKey privateKey) {
}
