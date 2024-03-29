package com.specificgroup.emotionstracker.entrylogging.state;


import com.specificgroup.emotionstracker.entrylogging.state.domain.State;
import com.specificgroup.emotionstracker.entrylogging.state.domain.EntryLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EntryLogServiceImpl implements EntryLogService {
    private final EntryLogRepository entryLogRepository;
    private final EntryLogEventPublisher entryLogEventPublisher;

    @Override
    public EntryLog acceptNewEntry(EntryLogDTO entryLogDTO) {
        EntryLog entryLog = new EntryLog(null,
                entryLogDTO.getUserId(),
                entryLogDTO.getState(),
                entryLogDTO.getEmotions(),
                entryLogDTO.getDateTime() == null ?
                LocalDateTime.now() :
                entryLogDTO.getDateTime());

        log.info("Adding new entry: {}", entryLog);

        // storing the state log
        EntryLog storedLog = entryLogRepository.save(entryLog);

        // publishing an event of logged state to notify subscribers
        entryLogEventPublisher.stateLogged(entryLog);

        return storedLog;
    }

    @Override
    public List<EntryLog> getLastLogsForUser(String userId) {
        List<EntryLog> lastLogs = entryLogRepository.findTop10ByUserIdOrderByDateTimeDesc(userId);
        log.info("Found last logs: {}", lastLogs);
        return lastLogs;
    }

    @Override
    public WeeklyStats getWeeklyStatsForUser(String userId) {

        List<EntryLog> entryLogs = entryLogRepository
                .findAllByUserIdAndDateTimeAfter(userId,
                        LocalDateTime.now().minus(Period.ofDays(7)));

        WeeklyStats stats = countStatsForUser(entryLogs);

        return stats;
    }

    private WeeklyStats countStatsForUser(List<EntryLog> entryLogs) {
        long awfulStates = entryLogs.stream().filter(l -> l.getState().equals(State.AWFUL)).count();
        long badStates = entryLogs.stream().filter(l -> l.getState().equals(State.BAD)).count();
        long okStates = entryLogs.stream().filter(l -> l.getState().equals(State.OK)).count();
        long goodStates = entryLogs.stream().filter(l -> l.getState().equals(State.GOOD)).count();
        long excellentStates = entryLogs.stream().filter(l -> l.getState().equals(State.EXCELLENT)).count();

        int total = (int) (awfulStates + badStates + okStates + goodStates + excellentStates);

        BigDecimal awfulPercentage = getPercentage(awfulStates, total);
        BigDecimal badPercentage = getPercentage(badStates, total);
        BigDecimal okPercentage = getPercentage(okStates, total);
        BigDecimal goodPercentage = getPercentage(goodStates, total);
        BigDecimal excellentPercentage = getPercentage(excellentStates, total);

        return new WeeklyStats(awfulPercentage, badPercentage, okPercentage, goodPercentage, excellentPercentage);
    }

    private static BigDecimal getPercentage(long states, int total) {
        if (total != 0) {
            return BigDecimal.valueOf(100 * states)
                    .divide(BigDecimal.valueOf(total), 1, 1);
        }
        return BigDecimal.ZERO;
    }
}
