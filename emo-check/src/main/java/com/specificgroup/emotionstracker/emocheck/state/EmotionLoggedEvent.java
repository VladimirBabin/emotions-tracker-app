package com.specificgroup.emotionstracker.emocheck.state;

import lombok.Value;
import java.time.LocalDateTime;
@Value
public class EmotionLoggedEvent {

    long stateLogId;
    long userId;
    String userAlias;
    Emotion emotion;
    LocalDateTime dateTime;

}
