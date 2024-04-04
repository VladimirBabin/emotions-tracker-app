package com.specificgroup.emotionstracker.alerts.alert;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import com.specificgroup.emotionstracker.alerts.configuration.RsaPrivateKey;
import com.specificgroup.emotionstracker.alerts.configuration.RsaPublicKey;
import com.specificgroup.emotionstracker.alerts.configuration.UserAuthProvider;
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

import java.util.*;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class AlertControllerSecurityTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private RsaPublicKey rsaPublicKey;

    @Autowired
    private RsaPrivateKey rsaPrivateKey;
    @MockBean
    private StateAlertService stateAlertService;
    @MockBean
    private EmotionAlertService emotionAlertService;
    @MockBean
    private UserAuthProvider userAuthProvider;

    @Autowired
    private JacksonTester<List<String>> alertsJacksonTester;
    @Test
    void whenNoTokenPresentThenPostRequestFailsWithUnauthorized() throws Exception {
        // given
        String userId = UUID.randomUUID().toString();
        List<String> states = List.of(StateAlertType.LOW_STATE_TWICE_IN_TWO_DAYS.getDescription());
        given(stateAlertService.getLastAddedStateAlerts(userId))
                .willReturn(states);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/alerts/recent").with(csrf()).param("userId", userId))
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
        List<String> emotions = List.of(EmotionAlertType.STRESSED_ONCE_A_WEEK.getDescription());
        given(emotionAlertService.getLastAddedEmotionAlerts(userId))
                .willReturn(emotions);
        given(userAuthProvider.validateToken(token, Optional.of(userId)))
                .willReturn(authentication);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/alerts/recent").with(csrf()).param("userId", userId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
                alertsJacksonTester.write(emotions).getJson());
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