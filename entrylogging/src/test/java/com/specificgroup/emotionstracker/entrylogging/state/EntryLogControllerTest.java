package com.specificgroup.emotionstracker.entrylogging.state;

import com.specificgroup.emotionstracker.entrylogging.state.domain.Emotion;
import com.specificgroup.emotionstracker.entrylogging.state.domain.State;
import com.specificgroup.emotionstracker.entrylogging.state.domain.EntryLog;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters
@WebMvcTest(EntryLogController.class)
class EntryLogControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private EntryLogService entryLogService;

    @Autowired
    private JacksonTester<EntryLogDTO> stateLogDTOJacksonTester;
    @Autowired
    private JacksonTester<EntryLog> stateLogJacksonTester;

    @Autowired
    private JacksonTester<WeeklyStats> weeklyStatsJacksonTester;

    @Autowired
    private JacksonTester<List<EntryLog>> lastLoggedStatesJacksonTester;

    @Test
    void whenPostValidStateLogThenResponseOk() throws Exception {
        // given
        LocalDateTime dateTime = LocalDateTime.now();
        Set<Emotion> emotions = Set.of(Emotion.CONTENT, Emotion.HAPPY);
        EntryLogDTO entryLogDTO = new EntryLogDTO(1L, State.GOOD, emotions, dateTime);
        EntryLog expectedEntryLog = new EntryLog(1L, 1L, State.GOOD, emotions, dateTime);
        given(entryLogService.acceptNewEntry(entryLogDTO))
                .willReturn(expectedEntryLog);

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/state").contentType(MediaType.APPLICATION_JSON)
                                .content(stateLogDTOJacksonTester.write(entryLogDTO).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(stateLogJacksonTester.write(expectedEntryLog).getJson());
    }

    @Test
    void whenPostInvalidStateLogThenBadRequest() throws Exception {
        // given
        EntryLogDTO entryLogDTO = new EntryLogDTO(1L, null, null, LocalDateTime.now());

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/state").contentType(MediaType.APPLICATION_JSON)
                                .content(stateLogDTOJacksonTester.write(entryLogDTO).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void whenGetWeeklyStatsForExistingUserThenResponseOk() throws Exception {
        // given
        BigDecimal oneFifth = BigDecimal.valueOf(20);
        WeeklyStats stats = new WeeklyStats(oneFifth, oneFifth , oneFifth, oneFifth, oneFifth);
        given(entryLogService.getWeeklyStatsForUser(1L))
                .willReturn(stats);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/state/statistics/week").param("userId", "1"))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
                weeklyStatsJacksonTester.write(stats).getJson());
    }

    @Test
    void whenGetLastLoggedStateForExistingUserThenResponseOk() throws Exception {
        // given
        EntryLog log1 = new EntryLog(1L, 1L, State.GOOD,
                Set.of(Emotion.PEACEFUL, Emotion.HAPPY),
                LocalDateTime.now());
        EntryLog log2 = new EntryLog(2L, 1L, State.BAD,
                Set.of(Emotion.ANGRY, Emotion.HOPEFUL),
                LocalDateTime.now());
        List<EntryLog> entryLogs = List.of(log1, log2);
        given(entryLogService.getLastLogsForUser(1L))
                .willReturn(entryLogs);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/state/statistics/last").param("userId", "1"))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
                lastLoggedStatesJacksonTester.write(entryLogs).getJson());
    }
}