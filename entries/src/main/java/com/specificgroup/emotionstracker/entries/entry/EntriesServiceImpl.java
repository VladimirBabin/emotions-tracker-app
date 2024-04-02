package com.specificgroup.emotionstracker.entries.entry;


import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.domain.State;
import com.specificgroup.emotionstracker.entries.entry.dto.EntryDto;
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
public class EntriesServiceImpl implements EntriesService {
    private final EntriesRepository entriesRepository;
    private final EntriesEventPublisher entriesEventPublisher;

    @Override
    public Entry acceptNewEntry(EntryDto entryDto) {
        Entry entry = new Entry(null,
                entryDto.getUserId(),
                entryDto.getState(),
                entryDto.getEmotions(),
                entryDto.getDateTime() == null ?
                LocalDateTime.now() :
                entryDto.getDateTime());

        log.info("Adding new entry: {}", entry);

        // storing the state log
        Entry storedLog = entriesRepository.save(entry);

        // publishing an event of logged state to notify subscribers
        entriesEventPublisher.stateLogged(entry);

        return storedLog;
    }

    @Override
    public List<Entry> getLastLogsForUser(String userId) {
        List<Entry> lastLogs = entriesRepository.findTop10ByUserIdOrderByDateTimeDesc(userId);
        log.info("Found last logs: {}", lastLogs);
        return lastLogs;
    }

    @Override
    public WeeklyStats getWeeklyStatsForUser(String userId) {

        List<Entry> entries = entriesRepository
                .findAllByUserIdAndDateTimeAfter(userId,
                        LocalDateTime.now().minus(Period.ofWeeks(1)));

        WeeklyStats stats = countStatsForUser(entries);

        return stats;
    }

    private WeeklyStats countStatsForUser(List<Entry> entries) {
        long awfulStates = entries.stream().filter(l -> l.getState().equals(State.AWFUL)).count();
        long badStates = entries.stream().filter(l -> l.getState().equals(State.BAD)).count();
        long okStates = entries.stream().filter(l -> l.getState().equals(State.OK)).count();
        long goodStates = entries.stream().filter(l -> l.getState().equals(State.GOOD)).count();
        long excellentStates = entries.stream().filter(l -> l.getState().equals(State.EXCELLENT)).count();

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
