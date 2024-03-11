package com.github.vbabin.emocheck.state;

import com.github.vbabin.emocheck.user.User;
import com.github.vbabin.emocheck.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StateServiceImpl implements StateService {
    private final StateLogRepository stateLogRepository;
    private final UserRepository userRepository;

    @Override
    public StateLog acceptNewState(StateLogDTO stateLogDTO) {
        User user = userRepository.findByAlias(stateLogDTO.getUserAlias())
                .orElseGet(() -> {
                    log.info("Creating new user with alias {}",
                            stateLogDTO.getUserAlias());
                    return new User(stateLogDTO.getUserAlias());
                });
        StateLog storedLog = new StateLog(null,
                user,
                stateLogDTO.getState(),
                stateLogDTO.getDateTime() == null ?
                LocalDateTime.now() :
                stateLogDTO.getDateTime());
        stateLogRepository.save(storedLog);

        // TODO check conditions for the reports

        return storedLog;
    }

    @Override
    public WeeklyStats getWeeklyStatsForUser(String alias) {
        User user = userRepository.findByAlias(alias)
                .orElseThrow(() -> new IllegalArgumentException("No user found by alias " + alias));

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

        int badPercentage = getPercentage(badStates, total);
        int goodPercentage = getPercentage(goodStates, total);
        int excellentPercentage = getPercentage(excellentStates, total);

        return new WeeklyStats(badPercentage, goodPercentage, excellentPercentage);
    }

    private static int getPercentage(long states, int total) {
        return (int) (100 * states / total);
    }
}
