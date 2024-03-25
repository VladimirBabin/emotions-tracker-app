package com.specificgroup.emotionstracker.entrylogging.state;

import com.specificgroup.emotionstracker.entrylogging.state.domain.EntryLog;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EntryLogRepository extends CrudRepository<EntryLog, Long> {
    List<EntryLog> findAllByUserIdAndDateTimeAfter(Long userId, LocalDateTime dateTime);
    List<EntryLog> findTop10ByUserIdOrderByDateTimeDesc(Long userId);
}
