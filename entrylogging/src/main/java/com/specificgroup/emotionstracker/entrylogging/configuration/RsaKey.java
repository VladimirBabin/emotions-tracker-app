package com.specificgroup.emotionstracker.entrylogging.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RsaKey(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
