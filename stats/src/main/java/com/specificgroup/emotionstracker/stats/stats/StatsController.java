package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.Emotion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * This class implements a REST API to GET statistics.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/stats")
public class StatsController {
    private final StateStatsService stateService;
    private final EmotionStatsService emotionService;

    @GetMapping("/state/week")
    ResponseEntity<WeeklyStats> getWeeklyStats(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(stateService.getWeeklyStatsForUser(userId));
    }

    @GetMapping("emotion/week/top")
    ResponseEntity<Set<Emotion>> getTopLoggedEmotions(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(emotionService.getLastWeekMostLoggedEmotions(userId));
    }
}
