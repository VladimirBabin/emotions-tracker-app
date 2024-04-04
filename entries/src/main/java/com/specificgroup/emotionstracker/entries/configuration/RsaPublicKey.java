package com.specificgroup.emotionstracker.entries.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa-public")
public record RsaPublicKey(RSAPublicKey publicKey) {
}
