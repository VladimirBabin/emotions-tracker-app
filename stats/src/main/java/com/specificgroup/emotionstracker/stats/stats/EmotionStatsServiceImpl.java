package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmotionStatsServiceImpl implements EmotionStatsService {

    private final EmotionEntryRepository entryRepository;

    @Override
    public void newEmotionLogForUser(EmotionLoggedEvent event) {
        entryRepository.save(new EmotionEntry(null,
                event.getUserId(),
                event.getEmotion(),
                event.getDateTime()));
    }
}
