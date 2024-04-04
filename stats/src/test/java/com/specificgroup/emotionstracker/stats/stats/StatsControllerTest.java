package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.Emotion;
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
import java.util.List;
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
    private StateStatsService stateService;
    @MockBean
    private EmotionStatsService emotionService;
    @Autowired
    private JacksonTester<WeeklyStats> weeklyStatsJacksonTester;
    @Autowired
    private JacksonTester<List<Emotion>> emotionsListJacksonTester;

    @Test
    void whenGetWeeklyStatsThenResponseOk() throws Exception {
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
        then(response.getContentAsString())
                .isEqualTo(weeklyStatsJacksonTester.write(stats).getJson());
    }

    @Test
    void whenGetTopLoggedEmotionsThenResponseOk() throws Exception {
        // given
        String userId = UUID.randomUUID().toString();
        List<Emotion> topEmotions = List.of(
                Emotion.CONTENT,
                Emotion.PEACEFUL,
                Emotion.PASSIONATE,
                Emotion.SCARED,
                Emotion.STRESSED);
        given(emotionService.getLastWeekMostLoggedEmotions(userId))
                .willReturn(topEmotions);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/stats/emotion/week/top").param("userId", userId))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString())
                .isEqualTo(emotionsListJacksonTester.write(topEmotions).getJson());
    }
}