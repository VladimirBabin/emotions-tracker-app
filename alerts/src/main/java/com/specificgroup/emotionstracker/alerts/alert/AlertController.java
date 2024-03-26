package com.specificgroup.emotionstracker.alerts.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<String>> getStateAlerts(@RequestParam("userId") long userId) {
        List<String> states = stateAlertService.getLastAddedStateAlerts(userId);
        List<String> emotions = emotionAlertService.getLastAddedEmotionAlerts(userId);
        List<String> result = new ArrayList<>(states);
        result.addAll(emotions);
        log.info("Fetching alerts: {}", result);
        return ResponseEntity.ok(result);
    }
}