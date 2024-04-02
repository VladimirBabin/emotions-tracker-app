package com.specificgroup.emotionstracker.stats.entry;

public enum Emotion {
    HAPPY (false),
    INDIFFERENT (false),
    SAD (true),
    EXCITED (false),
    PEACEFUL (false),
    ANXIOUS (true),
    SATISFIED (false),
    CONTENT (false),
    DRAINED (true),
    PASSIONATE (false),
    STRESSED (true),
    ANGRY (true),
    TIRED (true),
    HOPEFUL (false),
    IRRITATED (true),
    SURPRISED (false),
    SCARED (true),
    JEALOUS (false);

    Emotion(boolean alertTriggering) {
        this.alertTriggering = alertTriggering;
    }

    private boolean alertTriggering;

    public boolean isAlertTriggering() {
        return alertTriggering;
    }
}
