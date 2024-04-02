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
 * Identifies a state log received from a StateLoggedEvent.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateEntry {
    @Id
    @GeneratedValue
    private Long id;
    private String userId;
    private State state;
    @EqualsAndHashCode.Exclude
    private LocalDateTime dateTime;
}
