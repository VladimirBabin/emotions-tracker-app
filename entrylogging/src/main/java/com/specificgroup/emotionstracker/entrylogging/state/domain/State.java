package com.specificgroup.emotionstracker.entrylogging.state.domain;

/**
 * Enumeration with different type of states a user can log.
 */
public enum State {
    AWFUL(true), BAD(true), OK, GOOD, EXCELLENT;
    State() {
        this(false);
    }
    State(boolean alertTriggering) {
        this.alertTriggering = alertTriggering;
    }
    private final boolean alertTriggering;
    public boolean isAlertTriggering() {
        return alertTriggering;
    }

}
