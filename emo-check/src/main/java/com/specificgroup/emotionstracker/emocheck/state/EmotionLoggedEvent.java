package com.specificgroup.emotionstracker.emocheck.state;

import lombok.Value;
import java.time.LocalDateTime;

/**
 * Class represents the event of logging an emotion for particular user.
 */
@Value
public class EmotionLoggedEvent {

    long stateLogId;
    long userId;
    String userAlias;
    Emotion emotion;
    LocalDateTime dateTime;

}
