package com.specificgroup.emotionstracker.entries.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
@Profile("!test")
public record RsaKey(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
