package com.example.newspeed.exception;

import com.example.newspeed.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
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
        return buildDetailErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
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
        String message = bindingResult.getFieldError().getDefaultMessage();
        return buildDetailErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * 접근 권한이 없을 때의 예외처리
     *
     * @param e 발생한 예외 객체
     * @return 상태메세지와 에러코드
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException e) {
        return buildDetailErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
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
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
        return buildDetailErrorResponse(message, status);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRequestException(InvalidRequestException e) {
        return buildDetailErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /**
     * 중복된 Email에 대한 예외 처리
     *
     * @author 이현하
     * @param e 발생한 예외 객체상태
     * @return 메세지와 에러코드 반환
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(DuplicateEmailException e) {
        return buildSimpleErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /**
     * 유효하지 않은 비밀번호 예외 처리
     *
     * @author 이현하
     * @param e 발생한 예외 객체상태
     * @return 메세지와 에러코드 반환
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(InvalidPasswordException e) {
        return buildSimpleErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /**
     * 로그인 실패 시 예외 처리
     *
     * @author 이현하
     * @param e 발생한 예외 객체상태
     * @return 메세지와 에러코드 반환
     */
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(LoginFailedException e) {
        return buildSimpleErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /**
     * 검색 결과가 없을 시 예외 처리
     *
     * @author 이현하
     * @param e 발생한 예외 객체상태
     * @return 메세지와 OK 상태 코드 반환
     */
    @ExceptionHandler(NoResultFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(NoResultFoundException e) {
        return buildSimpleErrorResponse(e.getMessage(), HttpStatus.OK);
    }

    //공통 응답 생성 메소드
    private ResponseEntity<Map<String, Object>> buildDetailErrorResponse(String message, HttpStatus status){
        return ResponseEntity.status(status).body(Map.of("message", message));
    }
    private ResponseEntity<Map<String, String>> buildSimpleErrorResponse(String message, HttpStatus status){
        return ResponseEntity.status(status).body(Map.of("message", message));
    }
}
