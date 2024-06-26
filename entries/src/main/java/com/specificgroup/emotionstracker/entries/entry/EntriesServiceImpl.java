package com.specificgroup.emotionstracker.entries.entry;


import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.dto.EntryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
                entryDto.getComment(),
                entryDto.getDateTime() == null ?
                LocalDateTime.now() :
                entryDto.getDateTime());

        log.info("Adding new entry: {}", entry);

        // storing the state log
        Entry storedLog = entriesRepository.save(entry);

        // publishing an event of logged state to notify subscribers
        log.info("Publishing entry with id {}", storedLog.getId());
        entriesEventPublisher.entryLogged(storedLog);

        return storedLog;
    }

    @Override
    public List<Entry> getLastLogsForUser(String userId) {
        List<Entry> lastLogs = entriesRepository.findTop10ByUserIdOrderByDateTimeDesc(userId);
        log.info("Found last logs: {}", lastLogs);
        return lastLogs;
    }

    @Override
    public void removeEntryById(Long entryId) {
        entriesRepository.deleteById(entryId);
        entriesEventPublisher.entryRemoved(entryId);
    }

    @Override
    public Optional<Entry> findByEntryId(Long entryId) {
        return entriesRepository.findById(entryId);
    }
}
