package com.specificgroup.emotionstracker.alerts.entry;

import lombok.Value;

import java.time.LocalDateTime;

/**
 * Class represents the event of logging an emotion for particular user.
 */
@Value
public class EmotionLoggedEvent {

    long entryId;
    String userId;
    Emotion emotion;
    LocalDateTime dateTime;

}
