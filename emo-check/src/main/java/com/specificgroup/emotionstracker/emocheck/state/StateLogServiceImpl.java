package com.specificgroup.emotionstracker.emocheck.state

-group.emotions-tracker.emocheck.state;


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
public class StateLogServiceImpl implements StateLogService {
    private final StateLogRepository stateLogRepository;
    private final UserRepository userRepository;

    @Override
    public StateLog acceptNewState(StateLogDTO stateLogDTO) {
        User user = userRepository.findByAlias(stateLogDTO.getUserAlias())
                .orElseGet(() -> {
                    log.info("Creating new user with alias {}",
                            stateLogDTO.getUserAlias());
                    return userRepository.save(new User(stateLogDTO.getUserAlias()));
                });
        StateLog stateLog = new StateLog(null,
                user,
                stateLogDTO.getState(),
                stateLogDTO.getDateTime() == null ?
                LocalDateTime.now() :
                stateLogDTO.getDateTime());
        StateLog storedLog = stateLogRepository.save(stateLog);

        // TODO check conditions for the reports

        return storedLog;
    }

    @Override
    public WeeklyStats getWeeklyStatsForUser(String alias) {
        User user = userRepository.findByAlias(alias)
                .orElseThrow(() -> new NonExistingUserException("No user found by alias " + alias));

        List<StateLog> stateLogs = stateLogRepository
                .findAllByUserAndDateTimeAfter(user,
                        LocalDateTime.now().minus(Period.ofDays(7)));

        WeeklyStats stats = countStatsForUser(stateLogs);

        return stats;
    }

    private WeeklyStats countStatsForUser(List<StateLog> stateLogs) {
        long badStates = stateLogs.stream().filter(l -> l.getState().equals(State.BAD)).count();
        long goodStates = stateLogs.stream().filter(l -> l.getState().equals(State.GOOD)).count();
        long excellentStates = stateLogs.stream().filter(l -> l.getState().equals(State.EXCELLENT)).count();

        int total = (int) (badStates + goodStates + excellentStates);

        BigDecimal badPercentage = getPercentage(badStates, total);
        BigDecimal goodPercentage = getPercentage(goodStates, total);
        BigDecimal excellentPercentage = getPercentage(excellentStates, total);

        return new WeeklyStats(badPercentage, goodPercentage, excellentPercentage);
    }

    private static BigDecimal getPercentage(long states, int total) {
        return BigDecimal.valueOf(100 * states)
                .divide(BigDecimal.valueOf(total), 1, 1);
    }
}
