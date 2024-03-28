package com.specificgroup.emotionstracker.authorization.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.specificgroup.emotionstracker.authorization.dto.UserDto;
import com.specificgroup.emotionstracker.authorization.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    private final RsaKeyProperties keyProperties;
    private final UserService userService;


    public String createToken(String login, String userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3_600_000);

        return JWT.create()
                .withIssuer(login)
                .withAudience(userId)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.RSA256(keyProperties.publicKey(), keyProperties.privateKey()));
    }

    public Authentication validateToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.RSA256(keyProperties.publicKey())).build();

        DecodedJWT decoded = verifier.verify(token);

        UserDto user = userService.findByLogin(decoded.getIssuer());
        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }
}
