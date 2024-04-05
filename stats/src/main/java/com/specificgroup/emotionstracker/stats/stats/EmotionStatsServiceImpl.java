package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.Emotion;
import com.specificgroup.emotionstracker.stats.entry.EmotionEntry;
import com.specificgroup.emotionstracker.stats.entry.EmotionEntryRepository;
import com.specificgroup.emotionstracker.stats.entry.EmotionLoggedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmotionStatsServiceImpl implements EmotionStatsService {

    private final EmotionEntryRepository entryRepository;

    @Transactional
    @Override
    public void newEmotionLogForUser(EmotionLoggedEvent event) {
        entryRepository.save(new EmotionEntry(null,
                event.getEntryId(),
                event.getUserId(),
                event.getEmotion(),
                event.getDateTime()));
    }

    @Override
    public List<Emotion> getLastWeekMostLoggedEmotions(String userId) {
        return entryRepository
                .findTopRepeatedEmotionEntriesGropedByEmotionsDesc(
                        userId, LocalDateTime.now().minus(Period.ofWeeks(1)))
                .stream()
                .limit(TOP_EMOTIONS_LIMIT)
                .toList();
    }

    @Transactional
    @Override
    public void removeEntryRelatedData(Long entryId) {
        entryRepository.deleteAllByEntryId(entryId);
    }
}
