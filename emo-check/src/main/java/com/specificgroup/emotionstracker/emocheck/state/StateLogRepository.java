package com.specificgroup.emotionstracker.emocheck.state;

import com.specificgroup.emotionstracker.emocheck.state.domain.StateLog;
import com.specificgroup.emotionstracker.emocheck.user.User;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StateLogRepository extends CrudRepository<StateLog, Long> {
    List<StateLog> findAllByUserAndDateTimeAfter(User user, LocalDateTime dateTime);
    List<StateLog> findTop10ByUserAliasOrderByDateTimeDesc(String userAlias);
}
