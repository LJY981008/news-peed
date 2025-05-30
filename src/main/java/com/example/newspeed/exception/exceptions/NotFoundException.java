package com.example.newspeed.exception.exceptions;

/**
 * <p>요청값이 존재하지 않을 때</p>
 *
 * @author 이준영
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
