package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.statealertprocessors.StateAlertProcessor;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import com.specificgroup.emotionstracker.alerts.entry.*;
import jakarta.transaction.Transactional;
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
    public static final int MINUTES_SPAN_FOR_RECENT_ALERT = 60;
    private final StateEntryRepository entryRepository;
    private final StateAlertRepository alertRepository;
    private final List<StateAlertProcessor> alertProcessors;

    @Override
    public List<String> getLastAddedStateAlerts(String userId) {
        List<StateAlert> recentAlertsForUser = alertRepository
                .getNotShownAlertsByUserIdAfterGivenLocalDateTime(userId,
                        LocalDateTime.now().minusMinutes(MINUTES_SPAN_FOR_RECENT_ALERT));

        if (!recentAlertsForUser.isEmpty()) {
            alertRepository.saveAll(recentAlertsForUser.stream().map(e ->
                            new StateAlert(e.getAlertId(), e.getUserId(),
                                    e.getLocalDateTime(), e.getStateAlertType(), true))
                    .toList());
        }

        return recentAlertsForUser.stream()
                .map(StateAlert::getStateAlertType)
                .map(StateAlertType::getDescription)
                .toList();
    }

    @Transactional
    @Override
    public void newTriggeringStateForUser(StateLoggedEvent event) {
        entryRepository.save(new StateEntry(null,
                event.getEntryId(),
                event.getUserId(),
                event.getState(),
                event.getDateTime()));

        processForStateAlerts(event);
    }

    @Transactional
    @Override
    public void removeEntryRelatedData(Long entryId) {
        entryRepository.deleteAllByEntryId(entryId);
    }

    private void processForStateAlerts(StateLoggedEvent event) {
        // get all logged triggering states for user
        List<StateEntry> userStateEntries = entryRepository
                .findByUserIdOrderByDateTime(event.getUserId());

        List<StateAlert> latestAlerts = alertRepository.getAlertsByUserIdAfterGivenLocalDateTime(
                event.getUserId(), LocalDateTime.now().minusDays(DAYS_BEFORE_ALERT_CAN_REPEAT));

        log.info("found latest alerts: {}", latestAlerts);

        // check if user is eligible for new alerts, persist them and return
        List<StateAlert> newStateAlerts = alertProcessors.stream()
                .map(p -> p.processForOptionalAlertWithCheck(userStateEntries, latestAlerts))
                .flatMap(Optional::stream)
                .map(stateAlertType -> new StateAlert(event.getUserId(), stateAlertType))
                .toList();
        if (!newStateAlerts.isEmpty()) {
            alertRepository.saveAll(newStateAlerts);
        }
    }
}
