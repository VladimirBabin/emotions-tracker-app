package com.specificgroup.emotionstracker.emocheck.state.domain;

/**
 * Enumeration with different type of emotions a user can log. Emotion can be classified as alert triggering.
 */
public enum Emotion {
    HAPPY,
    INDIFFERENT,
    SAD (true),
    EXCITED,
    PEACEFUL,
    ANXIOUS (true),
    SATISFIED,
    CONTENT,
    DRAINED (true),
    PASSIONATE,
    STRESSED (true),
    ANGRY (true),
    TIRED (true),
    HOPEFUL,
    IRRITATED (true),
    SURPRISED,
    SCARED (true),
    JEALOUS;

    Emotion() {
        this(false);
    }

    Emotion(boolean alertTriggering) {
        this.alertTriggering = alertTriggering;
    }

    private boolean alertTriggering;

    public boolean isAlertTriggering() {
        return alertTriggering;
    }
}
