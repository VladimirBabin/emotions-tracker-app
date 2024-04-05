package com.specificgroup.emotionstracker.entries.entry;

import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.dto.EntryDto;

import java.util.List;
import java.util.Optional;

public interface EntriesService {

    /**
     * Accepts a new state log coming from presentation layer.
     * @return the resulting StateLog object.
     */
    Entry acceptNewEntry(EntryDto entryDto);

    /**
     * Retrieves the last 10 logged states for a particular user.
     */
    List<Entry> getLastLogsForUser(String userId);

    void removeEntryById(Long entryId);

    Optional<Entry> findByEntryId(Long entryId);
}
