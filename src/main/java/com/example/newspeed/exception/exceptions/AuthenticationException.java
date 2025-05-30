package com.example.newspeed.exception.exceptions;

/**
 * <p>접근 권한 없을 때 예외</p>
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
