package com.specificgroup.emotionstracker.entries.entry;

import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.dto.EntryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * This class implements a REST API to POST state logs from users and GET statistics.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/entries")
public class EntriesController {
    private final EntriesService entriesService;

    @PostMapping
    ResponseEntity<Entry> acceptStateLog(@RequestBody @Valid EntryDto entryDto) {
        log.info("New log entry attempt: {}", entryDto);
        return ResponseEntity.ok(entriesService.acceptNewEntry(entryDto));
    }


    @GetMapping("/last")
    ResponseEntity<List<Entry>> getLastLoggedStates(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(entriesService.getLastLogsForUser(userId));
    }

    @DeleteMapping
    ResponseEntity<String> deleteEntryById(@RequestParam("entryId") Long entryId) {
        Optional<Entry> existingEntry = entriesService.findByEntryId(entryId);
        if (existingEntry.isPresent()) {
            entriesService.removeEntryById(entryId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
