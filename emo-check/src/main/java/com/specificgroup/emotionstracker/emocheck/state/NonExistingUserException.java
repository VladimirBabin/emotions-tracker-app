package com.specificgroup.emotionstracker.emocheck.state;

/**
 * This exception is thrown when there is no such user in the database when getting the statistics for user's logs
 */
public class NonExistingUserException extends IllegalArgumentException{
    public NonExistingUserException(String s) {
        super(s);
    }
}
