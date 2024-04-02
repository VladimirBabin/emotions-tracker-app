package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.Emotion;
import com.specificgroup.emotionstracker.stats.entry.EmotionEntry;
import com.specificgroup.emotionstracker.stats.entry.EmotionEntryRepository;
import com.specificgroup.emotionstracker.stats.entry.EmotionLoggedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmotionStatsServiceImpl implements EmotionStatsService {

    public static final int TOP_EMOTIONS_LIMIT = 5;
    private final EmotionEntryRepository entryRepository;

    @Override
    public void newEmotionLogForUser(EmotionLoggedEvent event) {
        entryRepository.save(new EmotionEntry(null,
                event.getUserId(),
                event.getEmotion(),
                event.getDateTime()));
    }

    @Override
    public Set<Emotion> getLastWeekMostLoggedEmotions(String userId) {
        return entryRepository
                .findTopRepeatedEmotionEntriesGropedByEmotionsDesc(userId, LocalDateTime.now().minus(Period.ofWeeks(1)))
                .stream()
                .map(EmotionEntry::getEmotion)
                .collect(Collectors.groupingBy(e -> e, counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .limit(TOP_EMOTIONS_LIMIT)
                .collect(Collectors.toSet());
    }
}
