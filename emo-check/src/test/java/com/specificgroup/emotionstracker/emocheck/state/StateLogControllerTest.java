package com.specificgroup.emotionstracker.emocheck.state;

import com.specificgroup.emotionstracker.emocheck.user.User;
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

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters
@WebMvcTest(StateLogController.class)
class StateLogControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private StateLogService stateLogService;

    @Autowired
    private JacksonTester<StateLogDTO> stateLogDTOJacksonTester;
    @Autowired
    private JacksonTester<StateLog> stateLogJacksonTester;

    @Autowired
    private JacksonTester<WeeklyStats> weeklyStatsJacksonTester;

    @Test
    void postValidStateLog() throws Exception {
        // given
        User existingUser = new User(1L, "john_doe");
        LocalDateTime dateTime = LocalDateTime.now();
        StateLogDTO stateLogDTO = new StateLogDTO("john_doe", State.GOOD, dateTime);
        StateLog expectedStateLog = new StateLog(1L, existingUser, State.GOOD, dateTime);
        given(stateLogService.acceptNewState(stateLogDTO))
                .willReturn(expectedStateLog);

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/state").contentType(MediaType.APPLICATION_JSON)
                                .content(stateLogDTOJacksonTester.write(stateLogDTO).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(stateLogJacksonTester.write(expectedStateLog).getJson());
    }

    @Test
    void postInvalidStateLog() throws Exception {
        // given
        StateLogDTO stateLogDTO = new StateLogDTO("", null, LocalDateTime.now());

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/state").contentType(MediaType.APPLICATION_JSON)
                                .content(stateLogDTOJacksonTester.write(stateLogDTO).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void checkGettingWeeklyStatsForExistingUser() throws Exception {
        // given
        BigDecimal oneThird = BigDecimal.valueOf(33.3);
        WeeklyStats stats = new WeeklyStats(oneThird, oneThird , oneThird);
        given(stateLogService.getWeeklyStatsForUser("john_doe"))
                .willReturn(stats);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/state/statistics/week").param("alias", "john_doe"))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
                weeklyStatsJacksonTester.write(stats).getJson());
    }

    @Test
    void checkGettingWeeklyStatsForNonExistingUser() throws Exception {
        // given
        given(stateLogService.getWeeklyStatsForUser("john_doe"))
                .willThrow(new NonExistingUserException("No user found with alias john_doe"));

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/state/statistics/week").param("alias", "john_doe")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}