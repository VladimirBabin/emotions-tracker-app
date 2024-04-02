package com.specificgroup.emotionstracker.entries.entry;

import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EntriesRepository extends CrudRepository<Entry, Long> {
    List<Entry> findAllByUserIdAndDateTimeAfter(String userId, LocalDateTime dateTime);
    List<Entry> findTop10ByUserIdOrderByDateTimeDesc(String userId);
}
