package com.specificgroup.emotionstracker.entries.entry;

import com.specificgroup.emotionstracker.entries.entry.domain.Emotion;
import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.domain.State;
import com.specificgroup.emotionstracker.entries.entry.dto.EntryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureJsonTesters
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class EntryControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private EntriesService entriesService;
    @Autowired
    private JacksonTester<EntryDto> entryDtoJacksonTester;
    @Autowired
    private JacksonTester<Entry> entryJacksonTester;
    @Autowired
    private JacksonTester<List<Entry>> lastLoggedStatesJacksonTester;

    @Test
    void whenPostValidEntryThenResponseOk() throws Exception {
        // given
        String userId = UUID.randomUUID().toString();
        LocalDateTime dateTime = LocalDateTime.now();
        Set<Emotion> emotions = Set.of(Emotion.CONTENT, Emotion.HAPPY);
        EntryDto entryDto = new EntryDto(userId, State.GOOD, emotions,null, dateTime);
        Entry expectedEntry = new Entry(1L, userId, State.GOOD, emotions, null, dateTime);
        given(entriesService.acceptNewEntry(entryDto))
                .willReturn(expectedEntry);

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/entries").contentType(MediaType.APPLICATION_JSON)
                                .content(entryDtoJacksonTester.write(entryDto).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(entryJacksonTester.write(expectedEntry).getJson());
    }

    @Test
    void whenPostInvalidEntryThenBadRequest() throws Exception {
        // given
        String userId = UUID.randomUUID().toString();
        EntryDto entryDto = new EntryDto(userId, null, null, null, LocalDateTime.now());

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/entries").contentType(MediaType.APPLICATION_JSON)
                                .content(entryDtoJacksonTester.write(entryDto).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    void whenGetLastLoggedEntriesForExistingUserThenResponseOk() throws Exception {
        // given
        String userId = UUID.randomUUID().toString();
        Entry log1 = new Entry(1L, userId, State.GOOD,
                Set.of(Emotion.PEACEFUL, Emotion.HAPPY),
                null,
                LocalDateTime.now());
        Entry log2 = new Entry(2L, userId, State.BAD,
                Set.of(Emotion.ANGRY, Emotion.HOPEFUL),
                null,
                LocalDateTime.now());
        List<Entry> entries = List.of(log1, log2);
        given(entriesService.getLastLogsForUser(userId))
                .willReturn(entries);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/entries/last").param("userId", userId))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
                lastLoggedStatesJacksonTester.write(entries).getJson());
    }

    @Test
    void whenDeleteEntryThenEntryServiceRemoveEntryMethodCalled() throws Exception {
        // given
        Long entryId = 1L;
        Entry entry = new Entry(entryId, null, null, null, null, null);
        given(entriesService.findByEntryId(entryId))
                .willReturn(Optional.of(entry));

        // when
        MockHttpServletResponse response = mvc.perform(
                        delete("/entries").param("entryId", String.valueOf(entryId)))
                .andReturn().getResponse();

        // then
        verify(entriesService).findByEntryId(entryId);
        verify(entriesService).removeEntryById(entryId);
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void whenDeleteNonExistentEntryNotFoundStatus() throws Exception {
        // given
        Long entryId = 1L;
        given(entriesService.findByEntryId(entryId))
                .willReturn(Optional.empty());

        // when
        MockHttpServletResponse response = mvc.perform(
                        delete("/entries").param("entryId", String.valueOf(entryId)))
                .andReturn().getResponse();

        // then
        verify(entriesService).findByEntryId(entryId);
        verify(entriesService, never()).removeEntryById(anyLong());
        then(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}