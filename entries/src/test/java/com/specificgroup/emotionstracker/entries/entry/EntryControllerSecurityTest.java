package com.specificgroup.emotionstracker.entries.entry;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.specificgroup.emotionstracker.entries.configuration.RsaPrivateKey;
import com.specificgroup.emotionstracker.entries.configuration.RsaPublicKey;
import com.specificgroup.emotionstracker.entries.entry.domain.Emotion;
import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.domain.State;
import com.specificgroup.emotionstracker.entries.entry.dto.EntryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class EntryControllerSecurityTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private EntriesService entriesService;
    @Autowired
    private RsaPublicKey rsaPublicKey;
    @Autowired
    private RsaPrivateKey rsaPrivateKey;
    @Autowired
    private JacksonTester<EntryDto> entryDtoJacksonTester;
    @Autowired
    private JacksonTester<Entry> entryJacksonTester;
    @Test
    void whenNoTokenPresentThenPostRequestFailsWithUnauthorized() throws Exception {
        // given
        String userId = UUID.randomUUID().toString();
        LocalDateTime dateTime = LocalDateTime.now();
        Set<Emotion> emotions = Set.of(Emotion.CONTENT, Emotion.HAPPY);
        EntryDto entryDto = new EntryDto(userId, State.GOOD, emotions,null, dateTime);
        Entry expectedEntry = new Entry(1L, userId, State.GOOD, emotions, null, dateTime);
        given(entriesService.acceptNewEntry(entryDto))
                .willReturn(expectedEntry);

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/entries").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                                .content(entryDtoJacksonTester.write(entryDto).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void whenTokenPresentThenPostRequestSuccessful() throws Exception {
        // given
        String userId = UUID.randomUUID().toString();
        String token = createToken(userId);

        EntryDto entryDto = new EntryDto(userId, State.GOOD, null,null, null);
        Entry expectedEntry = new Entry(1L, userId, State.GOOD, null, null, null);
        given(entriesService.acceptNewEntry(entryDto))
                .willReturn(expectedEntry);

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/entries").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                                .content(entryDtoJacksonTester.write(entryDto).getJson())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(entryJacksonTester.write(expectedEntry).getJson());
    }

    private String createToken(String userId) {
        Date now = new Date();
        return JWT.create()
                .withIssuer("john_doe")
                .withAudience(userId)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + 10000))
                .sign(Algorithm.RSA256(rsaPublicKey.publicKey(), rsaPrivateKey.privateKey()));
    }
}
