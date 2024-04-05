package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.State;
import com.specificgroup.emotionstracker.stats.entry.StateEntry;
import com.specificgroup.emotionstracker.stats.entry.StateEntryRepository;
import com.specificgroup.emotionstracker.stats.entry.StateLoggedEvent;
import jakarta.transaction.Transactional;
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
public class StateStatsServiceImpl implements StateStatsService {

    private final StateEntryRepository entryRepository;

    @Transactional
    @Override
    public void newStateLogForUser(StateLoggedEvent event) {
        entryRepository.save(new StateEntry(null,
                event.getEntryId(),
                event.getUserId(),
                event.getState(),
                event.getDateTime()));
    }

    @Override
    public WeeklyStats getWeeklyStatsForUser(String userId) {

        List<StateEntry> entries = entryRepository
                .findAllByUserIdAndDateTimeAfter(userId,
                        LocalDateTime.now().minus(Period.ofWeeks(1)));

        return countStatsForUser(entries);
    }

    @Transactional
    @Override
    public void removeEntryRelatedData(Long entryId) {
        entryRepository.deleteAllByEntryId(entryId);
    }

    private WeeklyStats countStatsForUser(List<StateEntry> entries) {
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
