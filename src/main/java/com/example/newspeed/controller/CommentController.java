package com.example.newspeed.controller;

import com.example.newspeed.constant.Const;
import com.example.newspeed.dto.comment.*;
import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>댓글 관련 컨트롤러</p>
 *
 * @author 이준영
 */
@RestController
@RequestMapping(Const.COMMENT_URL)
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentCreateResponseDto> createComment(
            @RequestParam Long postId,
            @Valid @RequestBody CommentCreateRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        CommentCreateResponseDto responseComment = commentService.createComment(postId, requestDto, userDto);
        return responseEntity(HttpStatus.CREATED, responseComment);
    }

    // 포스트의 댓글 전체 조회
    @GetMapping
    public ResponseEntity<List<CommentFindResponseDto>> findCommentsByPostId(
            @RequestParam Long postId
    ) {
        List<CommentFindResponseDto> responseComments = commentService.findCommentByPostId(postId);
        return responseEntity(HttpStatus.OK,  responseComments);
    }

    // 댓글 수정
    @PatchMapping
    public ResponseEntity<CommentUpdateResponseDto> updateComment(
            @RequestParam Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        CommentUpdateResponseDto responseComment = commentService.updateComment(commentId, requestDto, userDto);
        return responseEntity(HttpStatus.OK, responseComment);
    }

    // 댓글 삭제
    @DeleteMapping
    public ResponseEntity<CommentDeleteResponseDto> deleteComment(
            @RequestParam Long commentId,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        CommentDeleteResponseDto responseComment = commentService.deleteComment(commentId, userDto);
        return responseEntity(HttpStatus.OK, responseComment);
    }

    /**
     * <p>응답 타입의 일관성을 위한 메서드</p>
     *
     * @param status 상태메시지
     * @param body 응답 Dto
     * @return 성공적으로 반환 했거나 실패 시 Dto의 타입 문제로 서버에러 반환
     * @param <T> {@link BaseCommentResponseDto}을 상속 받은 ResponseDto
     */
    private <T> ResponseEntity<T> responseEntity(HttpStatusCode status, T body) {
           return ResponseEntity.status(status).body(body);
    }
}
