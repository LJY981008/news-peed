package com.example.newspeed.exception;

import com.example.newspeed.exception.exceptions.AuthenticationException;
import com.example.newspeed.exception.exceptions.InvalidRequestException;
import com.example.newspeed.exception.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import java.util.Optional;


/**
 * 전역 예외처리 핸들러
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * {@link NotFoundException} 예외 처리
     *
     * @param e 발생한 예외 객체
     * @return 상태메세지와 에러코드 반환
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException e) {
        Map<String, Object> errors = Map.of("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    /**
     * Validation에 대한 예외 처리
     *
     * @param e 발생한 예외 객체
     * @return 상태메세지와 에러코드 반환
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, Object> errors = Map.of("message", bindingResult.getFieldError().getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * 접근 권한이 없을 때의 예외처리
     *
     * @param e 발생한 예외 객체
     * @return 상태메세지와 에러코드
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException e) {
        Map<String, Object> errors = Map.of("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

    /**{@link ResponseStatusException} 예외처리
     *
     * @param e 예외 객체
     * @return 상태메세지와 에러코드
     * @author 윤희준
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException e) {
        String message = Optional.ofNullable(e.getReason()).orElse("알 수 없는 오류가 발생했습니다.");
        Map<String, Object> errors = Map.of("message",message);
        return ResponseEntity.status(e.getStatusCode()).body(errors);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRequestException(InvalidRequestException e) {
        Map<String, Object> errors = Map.of("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
