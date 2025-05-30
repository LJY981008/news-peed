package com.example.newspeed.controller;

import com.example.newspeed.constant.Const;
import com.example.newspeed.dto.comment.*;
import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    /**
     * <p>댓글 생성</p>
     *
     * @param postId     댓글이 등록될 포스트의 ID
     * @param requestDto {@link CommentCreateRequestDto} 요청 DTO
     * @param userDto    로그인된 사용자 정보
     * @return {@link CommentCreateResponseDto} 반환 DTO
     */
    @PostMapping
    public ResponseEntity<CommentCreateResponseDto> createComment(
            @RequestParam Long postId,
            @Valid @RequestBody CommentCreateRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        CommentCreateResponseDto responseComment = commentService.createComment(postId, requestDto, userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseComment);
    }

    /**
     * <p>댓글 조회</p>
     *
     * @param postId 댓글을 조회할 포스트의 ID
     * @return {@link CommentFindResponseDto} 반환 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<List<CommentFindResponseDto>> findCommentByPostId(
            @RequestParam Long postId
    ) {
        List<CommentFindResponseDto> responseComments = commentService.findCommentByPostId(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseComments);
    }

    /**
     * <p>댓글 수정</p>
     *
     * @param commentId  댓글 Index
     * @param requestDto 수정 내용
     * @param userDto    로그인된 사용자 정보
     * @return {@link CommentUpdateResponseDto}
     */
    @PatchMapping
    public ResponseEntity<CommentUpdateResponseDto> updateComment(
            @RequestParam Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        CommentUpdateResponseDto responseComment = commentService.updateComment(commentId, requestDto, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseComment);
    }

    /**
     * <p>댓글 삭제</p>
     *
     * @param commentId 댓글 Index
     * @param userDto   로그인된 사용자 정보
     * @return {@link CommentDeleteResponseDto}
     */
    @DeleteMapping
    public ResponseEntity<CommentDeleteResponseDto> deleteComment(
            @RequestParam Long commentId,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        CommentDeleteResponseDto responseComment = commentService.deleteComment(commentId, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseComment);
    }
}
