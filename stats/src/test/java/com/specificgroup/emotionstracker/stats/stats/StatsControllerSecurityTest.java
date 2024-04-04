package com.specificgroup.emotionstracker.stats.stats;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.specificgroup.emotionstracker.stats.configuration.RsaPrivateKey;
import com.specificgroup.emotionstracker.stats.configuration.RsaPublicKey;
import com.specificgroup.emotionstracker.stats.configuration.UserAuthProvider;
import com.specificgroup.emotionstracker.stats.entry.Emotion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class StatsControllerSecurityTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    StateStatsService stateService;
    @MockBean
    EmotionStatsService emotionService;
    @MockBean
    private UserAuthProvider userAuthProvider;
    @Autowired
    RsaPrivateKey rsaPrivateKey;
    @Autowired
    RsaPublicKey rsaPublicKey;
    @Autowired
    private JacksonTester<List<Emotion>> emotionsListJacksonTester;

    @Test
    void whenNoTokenPresentThenPostRequestFailsWithUnauthorized() throws Exception {
        // given
        String userId = UUID.randomUUID().toString();
        BigDecimal oneFifth = BigDecimal.valueOf(20);
        WeeklyStats stats = new WeeklyStats(oneFifth, oneFifth , oneFifth, oneFifth, oneFifth);
        given(stateService.getWeeklyStatsForUser(userId))
                .willReturn(stats);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/stats/state/week").with(csrf()).param("userId", userId))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void whenTokenPresentThenGetRequestSuccessful() throws Exception {
        // given
        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken("john_doe", null, Collections.emptyList());
        String userId = UUID.randomUUID().toString();
        String token = createToken(userId);
        List<Emotion> topEmotions = List.of(
                Emotion.CONTENT,
                Emotion.PEACEFUL,
                Emotion.PASSIONATE,
                Emotion.SCARED,
                Emotion.STRESSED);
        given(emotionService.getLastWeekMostLoggedEmotions(userId))
                .willReturn(topEmotions);
        given(userAuthProvider.validateToken(token, Optional.of(userId)))
                .willReturn(authentication);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/stats/emotion/week/top").with(csrf()).param("userId", userId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString())
                .isEqualTo(emotionsListJacksonTester.write(topEmotions).getJson());
    }

    private String createToken(String userId) {
        Date now = new Date();
        return JWT.create()
                .withIssuer("john_doe")
                .withAudience(userId)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + 120))
                .sign(Algorithm.RSA256(rsaPublicKey.publicKey(), rsaPrivateKey.privateKey()));
    }
}