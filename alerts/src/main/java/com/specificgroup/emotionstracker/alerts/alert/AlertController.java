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

import java.util.List;

@RestController
@RequestMapping("/alerts")
@Slf4j
@RequiredArgsConstructor
public class AlertController {
    private final StateAlertService stateAlertService;
    private final EmotionAlertService emotionAlertService;

    @GetMapping("/state")
    public ResponseEntity<List<StateAlertType>> getStateAlerts(@RequestParam("id") long userId) {
        return ResponseEntity.ok(stateAlertService.getLastAddedStateAlerts(userId));
    }

    @GetMapping("/emotion")
    public ResponseEntity<List<EmotionAlertType>> getEmotionAlerts(@RequestParam("id") long userId) {
        return ResponseEntity.ok(emotionAlertService.getLastAddedEmotionAlerts(userId));
    }
}