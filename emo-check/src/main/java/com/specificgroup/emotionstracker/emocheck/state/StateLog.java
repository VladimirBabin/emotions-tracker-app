package com.specificgroup.emotionstracker.emocheck.state;

import com.specificgroup.emotionstracker.emocheck.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Identifies a state log from a {@link User}.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateLog {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    private State state;

    @ElementCollection(targetClass = Emotion.class, fetch = FetchType.LAZY)
    private Set<Emotion> emotions;
    private LocalDateTime dateTime;
}
