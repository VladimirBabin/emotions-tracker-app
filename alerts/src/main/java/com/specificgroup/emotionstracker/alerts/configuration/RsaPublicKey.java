package com.specificgroup.emotionstracker.alerts.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa-public")
public record RsaPublicKey(RSAPublicKey publicKey) {
}
