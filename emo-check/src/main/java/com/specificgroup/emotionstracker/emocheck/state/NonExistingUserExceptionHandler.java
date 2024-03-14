package com.specificgroup.emotionstracker.emocheck.state;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps exceptions to HTTP codes
 */
@RestControllerAdvice
public class NonExistingUserExceptionHandler {

    @ExceptionHandler(NonExistingUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNonExistingUserException() {}
}
