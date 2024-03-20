package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.statealertprocessors.StateAlertProcessor;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import com.specificgroup.emotionstracker.alerts.state.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StateAlertServiceImpl implements StateAlertService {
    public static final int DAYS_BEFORE_ALERT_CAN_REPEAT = 30;
    public static final int MINUTES_SPAN_FOR_ADDED_ALERT = 2;
    private final StateLogRepository logRepository;
    private final StateAlertRepository alertRepository;
    private final List<StateAlertProcessor> alertProcessors;

    @Override
    public List<StateAlertType> getLastAddedStateAlerts(Long userId) {
        return alertRepository
                .getAlertTypesByUserIdAfterGivenLocalDateTime(userId,
                        LocalDateTime.now().minusMinutes(MINUTES_SPAN_FOR_ADDED_ALERT));
    }

    @Override
    public void newTriggeringStateForUser(StateLoggedEvent event) {
        logRepository.save(new StateLog(null,
                event.getUserId(),
                event.getState(),
                event.getDateTime()));

        List<StateAlert> stateAlerts = processForStateAlerts(event);

        if (!stateAlerts.isEmpty()) {
            // make a WebSocket call to frontend
        }
    }

    private List<StateAlert> processForStateAlerts(StateLoggedEvent event) {
        // get all logged triggering states for user
        List<StateLog> userStateLogs = logRepository
                .findByUserOrderByDateTime(event.getUserId());

        List<StateAlert> latestAlerts = alertRepository.getAlertsByUserIdAfterGivenLocalDateTime(
                event.getUserId(), LocalDateTime.now().minusDays(DAYS_BEFORE_ALERT_CAN_REPEAT));

        // check if user is eligible for new alerts, persist them and return
        List<StateAlert> newStateAlerts = alertProcessors.stream()
                .map(p -> p.processForOptionalAlert(userStateLogs, latestAlerts))
                .flatMap(Optional::stream)
                .map(stateAlertType -> new StateAlert(event.getUserId(), stateAlertType))
                .toList();
        if (!newStateAlerts.isEmpty()) {
            alertRepository.saveAll(newStateAlerts);
        }

        return newStateAlerts;
    }
}
