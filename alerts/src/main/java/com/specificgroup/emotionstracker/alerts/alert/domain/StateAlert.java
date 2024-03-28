package com.specificgroup.emotionstracker.alerts.alert.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateAlert {

    @Id
    @GeneratedValue
    private Long alertId;
    private String userId;
    @EqualsAndHashCode.Exclude
    private LocalDateTime localDateTime;
    private StateAlertType stateAlertType;
    @EqualsAndHashCode.Exclude
    private boolean shown;

    public StateAlert(String userId, StateAlertType stateAlertType) {
        this(null, userId, LocalDateTime.now(), stateAlertType, false);
    }
}
