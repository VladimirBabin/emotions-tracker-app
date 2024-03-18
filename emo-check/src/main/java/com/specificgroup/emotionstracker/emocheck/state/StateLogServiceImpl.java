package com.specificgroup.emotionstracker.emocheck.state;


import com.specificgroup.emotionstracker.emocheck.user.User;
import com.specificgroup.emotionstracker.emocheck.user.UserRepository;
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
    private final StateLogEventPublisher stateLogEventPublisher;

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
                stateLogDTO.getEmotions(),
                stateLogDTO.getDateTime() == null ?
                LocalDateTime.now() :
                stateLogDTO.getDateTime());

        // storing the state log
        StateLog storedLog = stateLogRepository.save(stateLog);

        // publishing an event of logged state to notify subscribers
        stateLogEventPublisher.stateLogged(stateLog);

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
        long awfulStates = stateLogs.stream().filter(l -> l.getState().equals(State.AWFUL)).count();
        long badStates = stateLogs.stream().filter(l -> l.getState().equals(State.BAD)).count();
        long okStates = stateLogs.stream().filter(l -> l.getState().equals(State.OK)).count();
        long goodStates = stateLogs.stream().filter(l -> l.getState().equals(State.GOOD)).count();
        long excellentStates = stateLogs.stream().filter(l -> l.getState().equals(State.EXCELLENT)).count();

        int total = (int) (awfulStates + badStates + okStates + goodStates + excellentStates);

        BigDecimal awfulPercentage = getPercentage(awfulStates, total);
        BigDecimal badPercentage = getPercentage(badStates, total);
        BigDecimal okPercentage = getPercentage(okStates, total);
        BigDecimal goodPercentage = getPercentage(goodStates, total);
        BigDecimal excellentPercentage = getPercentage(excellentStates, total);

        return new WeeklyStats(awfulPercentage, badPercentage, okPercentage, goodPercentage, excellentPercentage);
    }

    private static BigDecimal getPercentage(long states, int total) {
        return BigDecimal.valueOf(100 * states)
                .divide(BigDecimal.valueOf(total), 1, 1);
    }
}
