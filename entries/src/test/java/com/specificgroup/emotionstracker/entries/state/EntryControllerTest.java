package com.specificgroup.emotionstracker.entries.state;

import com.specificgroup.emotionstracker.entries.entry.EntriesService;
import com.specificgroup.emotionstracker.entries.entry.domain.Emotion;
import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.domain.State;
import com.specificgroup.emotionstracker.entries.entry.dto.EntryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class EntryControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private EntriesService entriesService;

    @Autowired
    private JacksonTester<EntryDto> stateLogDTOJacksonTester;
    @Autowired
    private JacksonTester<Entry> stateLogJacksonTester;
    @Autowired
    private JacksonTester<List<Entry>> lastLoggedStatesJacksonTester;

    @Test
    void whenPostValidStateLogThenResponseOk() throws Exception {
        // given
        String userId = UUID.randomUUID().toString();
        LocalDateTime dateTime = LocalDateTime.now();
        Set<Emotion> emotions = Set.of(Emotion.CONTENT, Emotion.HAPPY);
        EntryDto entryDto = new EntryDto(userId, State.GOOD, emotions, dateTime);
        Entry expectedEntry = new Entry(1L, userId, State.GOOD, emotions, dateTime);
        given(entriesService.acceptNewEntry(entryDto))
                .willReturn(expectedEntry);

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/state").contentType(MediaType.APPLICATION_JSON)
                                .content(stateLogDTOJacksonTester.write(entryDto).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(stateLogJacksonTester.write(expectedEntry).getJson());
    }

    @Test
    void whenPostInvalidStateLogThenBadRequest() throws Exception {
        // given
        String userId = UUID.randomUUID().toString();
        EntryDto entryDto = new EntryDto(userId, null, null, LocalDateTime.now());

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/state").contentType(MediaType.APPLICATION_JSON)
                                .content(stateLogDTOJacksonTester.write(entryDto).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    void whenGetLastLoggedStateForExistingUserThenResponseOk() throws Exception {
        // given
        String userId = UUID.randomUUID().toString();
        Entry log1 = new Entry(1L, userId, State.GOOD,
                Set.of(Emotion.PEACEFUL, Emotion.HAPPY),
                LocalDateTime.now());
        Entry log2 = new Entry(2L, userId, State.BAD,
                Set.of(Emotion.ANGRY, Emotion.HOPEFUL),
                LocalDateTime.now());
        List<Entry> entries = List.of(log1, log2);
        given(entriesService.getLastLogsForUser(userId))
                .willReturn(entries);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/state/statistics/last").param("userId", userId))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
                lastLoggedStatesJacksonTester.write(entries).getJson());
    }
}