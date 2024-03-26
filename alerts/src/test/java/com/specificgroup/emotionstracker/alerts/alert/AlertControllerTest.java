package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
@AutoConfigureJsonTesters
class AlertControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private StateAlertService stateAlertService;

    @MockBean
    private EmotionAlertService emotionAlertService;

    @Autowired
    private JacksonTester<List<String>> alertsJacksonTester;

    @Test
    void whenGetPresentStateAlertsThenResponseIsOk() throws Exception {
        // given
        List<String> states = List.of(StateAlertType.LOW_STATE_TWICE_IN_TWO_DAYS.getDescription());
        given(stateAlertService.getLastAddedStateAlerts(1L))
                .willReturn(states);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/alerts/recent").param("userId", "1"))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
                alertsJacksonTester.write(states).getJson());
    }

    @Test
    void whenGetPresentEmotionAlertsThenResponseIsOk() throws Exception {
        // given
        List<String> emotions = List.of(EmotionAlertType.STRESSED_ONCE_A_WEEK.getDescription());
        given(emotionAlertService.getLastAddedEmotionAlerts(1L))
                .willReturn(emotions);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/alerts/recent").param("userId", "1"))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
                alertsJacksonTester.write(emotions).getJson());
    }

    @Test
    void whenNoAlertsPresentThenResponseIsOkAndEmptyListReturned() throws Exception {
        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/alerts/recent").param("userId", "1"))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString())
                .isEqualTo(alertsJacksonTester.write(List.of()).getJson());
    }
}