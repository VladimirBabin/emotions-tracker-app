package com.specificgroup.emotionstracker.alerts.entry;

import jakarta.persistence.*;
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
