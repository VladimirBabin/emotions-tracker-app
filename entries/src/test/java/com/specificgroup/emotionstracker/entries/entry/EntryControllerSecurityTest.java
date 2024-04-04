package com.specificgroup.emotionstracker.entries.entry;

import com.specificgroup.emotionstracker.entries.configuration.RsaKey;
import com.specificgroup.emotionstracker.entries.configuration.UserAuthProvider;
import com.specificgroup.emotionstracker.entries.entry.domain.Emotion;
import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.domain.State;
import com.specificgroup.emotionstracker.entries.entry.dto.EntryDto;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class EntryControllerSecurityTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private EntriesService entriesService;
    @Mock
    private UserAuthProvider userAuthProvider;

    @Mock
    RsaKey rsaKey;

    @Autowired
    private JacksonTester<EntryDto> stateLogDTOJacksonTester;
    @Autowired
    private JacksonTester<Entry> stateLogJacksonTester;
    @Autowired
    private JacksonTester<List<Entry>> lastLoggedStatesJacksonTester;

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
                                .content(stateLogDTOJacksonTester.write(entryDto).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void whenTokenPresentThenPostRequestSuccessful() throws Exception {
        // given
        OAuth2ResourceServerProperties.Jwt jwt = BDDMockito.mock();
        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken("john_doe", null, Collections.emptyList());
        String userId = UUID.randomUUID().toString();
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5NjVmYmU2Mi03NjU4LTQxOGItYWIyZS0yMzZmNDNmZmE5MWQiLCJpc3MiOiJtb2lzZXMiLCJleHAiOjE3MTE2MjAyNDYsImlhdCI6MTcxMTYxNjY0Nn0.dhgpa0zgyd7_w39GGO_V8cU6f0lcIsPA0XhmtISIppM";
        LocalDateTime dateTime = LocalDateTime.now();
        Set<Emotion> emotions = Set.of(Emotion.CONTENT, Emotion.HAPPY);
        EntryDto entryDto = new EntryDto(userId, State.GOOD, emotions,null, dateTime);
        Entry expectedEntry = new Entry(1L, userId, State.GOOD, emotions, null, dateTime);
        given(entriesService.acceptNewEntry(entryDto))
                .willReturn(expectedEntry);
        given(userAuthProvider.validateToken(token, Optional.of(userId)))
                .willReturn(authentication);
//        given(rsaKey.publicKey())
//                .willReturn(null);

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/entries").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                                .content(stateLogDTOJacksonTester.write(entryDto).getJson())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
