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
    private JacksonTester<List<StateAlertType>> stateAlertsJacksonTester;

    @Autowired
    private JacksonTester<List<EmotionAlertType>> emotionAlertsJacksonTester;

    @Test
    void whenGetStateAlertsThenResponseIsOk() throws Exception {
        // given
        List<StateAlertType> stateAlerts = List.of(StateAlertType.LOW_STATE_TWICE_IN_TWO_DAYS);
        given(stateAlertService.getLastAddedStateAlerts(1L))
                .willReturn(stateAlerts);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/alerts/state").param("id", "1"))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
                stateAlertsJacksonTester.write(stateAlerts).getJson());
    }

    @Test
    void whenGetEmotionAlertsThenResponseIsOk() throws Exception {
        // given
        List<EmotionAlertType> emotionAlerts = List.of(EmotionAlertType.STRESSED_ONCE_A_WEEK);
        given(emotionAlertService.getLastAddedEmotionAlerts(1L))
                .willReturn(emotionAlerts);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/alerts/emotion").param("id", "1"))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
                emotionAlertsJacksonTester.write(emotionAlerts).getJson());
    }
}