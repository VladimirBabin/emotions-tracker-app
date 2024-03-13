package com.github.vbabin.emocheck.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/state")
public class StateController {

    private final StateLogService stateLogService;

    @PostMapping
    ResponseEntity<StateLog> acceptStateLog(@RequestBody StateLogDTO stateLogDTO) {
        return ResponseEntity.ok(stateLogService.acceptNewState(stateLogDTO));
    }

    @GetMapping("/statistics/week")
    ResponseEntity<WeeklyStats> getWeeklyStats(@RequestParam("alias") String alias) {
        return ResponseEntity.ok(stateLogService.getWeeklyStatsForUser(alias));
    }


}
