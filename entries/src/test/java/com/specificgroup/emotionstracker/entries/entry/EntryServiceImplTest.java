package com.specificgroup.emotionstracker.entries.entry;

import com.specificgroup.emotionstracker.entries.entry.domain.Emotion;
import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.domain.State;
import com.specificgroup.emotionstracker.entries.entry.dto.EntryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class EntryServiceImplTest {

    private EntriesService entriesService;

    @Mock
    private EntriesRepository entriesRepository;
    @Mock
    private EntriesEventPublisher entriesEventPublisher;

    @BeforeEach
    void setUp() {
        entriesService = new EntriesServiceImpl(
                entriesRepository,
                entriesEventPublisher
        );
    }

    @Test
    void whenNewStateLoggedThenStateAndEmotionsPersistedAndEventPublished() {
        // given
        String userId = UUID.randomUUID().toString();
        Set<Emotion> emotions = Set.of(Emotion.HAPPY, Emotion.CONTENT);
        EntryDto entryDto = new EntryDto(userId, State.BAD, emotions, null, LocalDateTime.now());
        given(entriesRepository.save(any()))
                .will(returnsFirstArg());

        // when
        Entry entry = entriesService.acceptNewEntry(entryDto);

        // then
        then(entry.getState()).isEqualTo(State.BAD);
        then(entry.getUserId()).isEqualTo(userId);
        verify(entriesRepository).save(entry);
        verify(entriesEventPublisher).entryLogged(entry);
    }


    @Test
    void whenGetLastLogsForUserThenRetrievedSuccessfully() {
        // given
        String userId = UUID.randomUUID().toString();
        Entry log1 = new Entry(1L, userId, State.GOOD,
                Set.of(Emotion.PEACEFUL, Emotion.HAPPY),
                "comment1",
                LocalDateTime.now());
        Entry log2 = new Entry(2L, userId, State.BAD,
                Set.of(Emotion.ANGRY, Emotion.HOPEFUL),
                "comment2",
                LocalDateTime.now());
        List<Entry> entries = List.of(log1, log2);
        given(entriesRepository.findTop10ByUserIdOrderByDateTimeDesc(userId))
                .willReturn(entries);

        // when
        List<Entry> entryLogsResult = entriesService.getLastLogsForUser(userId);

        // then
        then(entryLogsResult).isEqualTo(entries);
    }

    @Test
    void whenRemoveByIdCalledRemovedFromRepositoryAndRemovedEventPublished() {
        // given
        Long entryId = 1L;

        // when
        entriesService.removeEntryById(entryId);

        // then
        verify(entriesRepository).deleteById(entryId);
        entriesEventPublisher.entryRemoved(entryId);
    }
}