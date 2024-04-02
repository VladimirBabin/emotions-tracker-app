package com.specificgroup.emotionstracker.stats.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class implements a REST API to GET statistics.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/state")
public class StateStatsController {

    private final StateStatsService stateService;

    @GetMapping("/statistics/week")
    ResponseEntity<WeeklyStats> getWeeklyStats(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(stateService.getWeeklyStatsForUser(userId));
    }
}
