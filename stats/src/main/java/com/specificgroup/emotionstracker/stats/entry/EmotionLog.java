package com.specificgroup.emotionstracker.stats.entry;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Identifies an emotion log received from an EmotionLoggedEvent.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmotionLog {
    @Id
    @GeneratedValue
    private Long id;
    private String userId;
    private Emotion emotion;
    @EqualsAndHashCode.Exclude
    private LocalDateTime dateTime;
}
