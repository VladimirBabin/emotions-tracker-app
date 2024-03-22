package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/alerts")
@Slf4j
@RequiredArgsConstructor
public class AlertController {
    private final StateAlertService stateAlertService;
    private final EmotionAlertService emotionAlertService;

    @GetMapping("/recent")
    public ResponseEntity<List<String>> getStateAlerts(@RequestParam("id") long userId) {
        List<StateAlertType> lastAddedStateAlerts = stateAlertService.getLastAddedStateAlerts(userId);
        List<String> states = lastAddedStateAlerts.stream().map(StateAlertType::getDescription).toList();
        List<EmotionAlertType> lastAddedEmotionAlerts = emotionAlertService.getLastAddedEmotionAlerts(userId);
        List<String> emotions = lastAddedEmotionAlerts.stream().map(EmotionAlertType::getDescription).toList();
        List<String> result = new ArrayList<>(states);
        result.addAll(emotions);
        log.info("Fetching results: {}", result);
        return ResponseEntity.ok(result);
    }
}