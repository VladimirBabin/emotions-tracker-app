package com.specificgroup.emotionstracker.alerts.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {
    private final RsaPublicKey key;

    public Authentication validateToken(String token, Optional<String> userId) {
        JWTVerifier verifier = JWT.require(Algorithm.RSA256(key.publicKey())).build();

        DecodedJWT decoded = verifier.verify(token);
        verifyAudience(userId, decoded);
        String login = decoded.getIssuer();

        return new UsernamePasswordAuthenticationToken(login, null, Collections.emptyList());
    }

    private static void verifyAudience(Optional<String> userId, DecodedJWT decoded) {
        List<String> audience = decoded.getAudience();
        if (userId.isPresent()) {
            if (!audience.contains(userId.get())) {
                throw new InvalidClaimException("Access denied: requested user id data isn't in the allowed audience");
            }
        }
    }
}
