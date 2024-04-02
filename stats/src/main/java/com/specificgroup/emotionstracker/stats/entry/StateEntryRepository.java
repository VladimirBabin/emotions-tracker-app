package com.specificgroup.emotionstracker.stats.entry;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StateEntryRepository extends CrudRepository<StateEntry, Long> {

    /**
     * Retrieves state logs for user identified by user id after given date time.
     */
    List<StateEntry> findAllByUserIdAndDateTimeAfter(String userId, LocalDateTime dateTime);
}
