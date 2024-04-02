package com.specificgroup.emotionstracker.stats.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RsaKey(RSAPublicKey publicKey) {
}
