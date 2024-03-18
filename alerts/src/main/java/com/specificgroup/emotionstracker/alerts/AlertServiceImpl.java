package com.specificgroup.emotionstracker.alerts;

import com.specificgroup.emotionstracker.alerts.state.EmotionLoggedEvent;
import com.specificgroup.emotionstracker.alerts.state.StateLoggedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    @Override
    public void newStateLogForUser(StateLoggedEvent stateLoggedEvent) {
        log.info("Alert Service: new state");
    }

    @Override
    public void newEmotionLogForUser(EmotionLoggedEvent emotionLoggedEvent) {
        log.info("Alert Service: new emotion");
    }
}
