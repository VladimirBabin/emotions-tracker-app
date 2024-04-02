package com.specificgroup.emotionstracker.stats.stats;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureJsonTesters
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class StatsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    StateStatsService stateService;

    @Autowired
    private JacksonTester<WeeklyStats> weeklyStatsJacksonTester;

    @Test
    void whenGetWeeklyStatsForExistingUserThenResponseOk() throws Exception {
        // given
        String userId = UUID.randomUUID().toString();
        BigDecimal oneFifth = BigDecimal.valueOf(20);
        WeeklyStats stats = new WeeklyStats(oneFifth, oneFifth , oneFifth, oneFifth, oneFifth);
        given(stateService.getWeeklyStatsForUser(userId))
                .willReturn(stats);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/stats/state/week").param("userId", userId))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
                weeklyStatsJacksonTester.write(stats).getJson());
    }

}