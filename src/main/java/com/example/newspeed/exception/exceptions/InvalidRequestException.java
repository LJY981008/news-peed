package com.example.newspeed.exception.exceptions;

/**
 * <p>유효하지않은 요청</p>
 *
 * @author 이준영
 */
public class InvalidRequestException extends RuntimeException {
  public InvalidRequestException(String message) {
    super(message);
  }
}
