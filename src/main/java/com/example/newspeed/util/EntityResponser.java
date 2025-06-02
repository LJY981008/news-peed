package com.example.newspeed.util;

import com.example.newspeed.dto.comment.BaseCommentResponseDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/**
 * <p>응답 타입의 일관성을 위한 클래스</p>
 */
public class EntityResponser {
    /**
     * <p>응답 메서드</p>
     *
     * @param status 상태메시지
     * @param body 응답 Dto
     * @return 성공적으로 반환 했거나 실패 시 Dto의 타입 문제로 서버에러 반환
     * @param <T> ResponseDto
     */
    public static <T> ResponseEntity<T> responseEntity(HttpStatusCode status, T body) {
        return ResponseEntity.status(status).body(body);
    }
}
