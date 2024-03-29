package com.specificgroup.emotionstracker.entrylogging.state;

import com.specificgroup.emotionstracker.entrylogging.state.domain.EntryLog;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class implements a REST API to POST state logs from users and GET statistics.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/state")
public class EntryLogController {
    private final EntryLogService entryLogService;
    @PostMapping
    ResponseEntity<EntryLog> acceptStateLog(@RequestBody @Valid EntryLogDTO entryLogDTO) {
        log.info("New log entry attempt: {}", entryLogDTO);
        return ResponseEntity.ok(entryLogService.acceptNewEntry(entryLogDTO));
    }

    @GetMapping("/statistics/week")
    ResponseEntity<WeeklyStats> getWeeklyStats(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(entryLogService.getWeeklyStatsForUser(userId));
    }

    @GetMapping("/statistics/last")
    ResponseEntity<List<EntryLog>> getLastLoggedStates(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(entryLogService.getLastLogsForUser(userId));
    }
}
