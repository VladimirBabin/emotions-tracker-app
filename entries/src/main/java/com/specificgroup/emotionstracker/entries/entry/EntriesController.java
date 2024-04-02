package com.specificgroup.emotionstracker.entries.entry;

import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.dto.EntryDto;
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
public class EntriesController {
    private final EntriesService entriesService;
    @PostMapping
    ResponseEntity<Entry> acceptStateLog(@RequestBody @Valid EntryDto entryDto) {
        log.info("New log entry attempt: {}", entryDto);
        return ResponseEntity.ok(entriesService.acceptNewEntry(entryDto));
    }

    @GetMapping("/statistics/week")
    ResponseEntity<WeeklyStats> getWeeklyStats(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(entriesService.getWeeklyStatsForUser(userId));
    }

    @GetMapping("/statistics/last")
    ResponseEntity<List<Entry>> getLastLoggedStates(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(entriesService.getLastLogsForUser(userId));
    }
}
