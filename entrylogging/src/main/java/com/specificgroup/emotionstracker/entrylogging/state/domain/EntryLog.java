package com.specificgroup.emotionstracker.entrylogging.state.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Identifies a state log linked to User by userId.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryLog {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private State state;

    @ElementCollection(targetClass = Emotion.class, fetch = FetchType.LAZY)
    private Set<Emotion> emotions;
    @EqualsAndHashCode.Exclude
    private LocalDateTime dateTime;
}
