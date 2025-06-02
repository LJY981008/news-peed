package com.example.newspeed.controller;

import com.example.newspeed.constant.Const;
import com.example.newspeed.dto.comment.*;
import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.service.CommentService;
import com.example.newspeed.util.EntityResponser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * 댓글 관련 HTTP 요청을 처리하는 컨트롤러
 * 댓글의 생성, 조회, 수정, 삭제 기능을 제공합니다.
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
     * 특정 게시글의 댓글 목록을 조회합니다.
     *
     * @param postId 게시글 ID
     * @return 댓글 목록과 HTTP 상태 코드를 포함한 ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<CommentFindResponseDto>> findCommentsByPostId(
            @RequestParam Long postId
    ) {
        List<CommentFindResponseDto> responseComments = commentService.findCommentByPostId(postId);
        return EntityResponser.responseEntity(HttpStatus.OK,  responseComments);
    }

    /**
     * 새로운 댓글을 생성합니다.
     *
     * @param postId 게시글 ID
     * @param requestDto 댓글 생성 요청 데이터
     * @return 생성된 댓글 정보와 HTTP 상태 코드를 포함한 ResponseEntity
     */
    @PostMapping
    public ResponseEntity<CommentCreateResponseDto> createComment(
            @RequestParam Long postId,
            @Valid @RequestBody CommentCreateRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        CommentCreateResponseDto responseComment = commentService.createComment(postId, requestDto, userDto);
        return EntityResponser.responseEntity(HttpStatus.CREATED, responseComment);
    }

    /**
     * 기존 댓글을 수정합니다.
     *
     * @param commentId 수정할 댓글 ID
     * @param requestDto 댓글 수정 요청 데이터
     * @return 수정된 댓글 정보와 HTTP 상태 코드를 포함한 ResponseEntity
     */
    @PatchMapping
    public ResponseEntity<CommentUpdateResponseDto> updateComment(
            @RequestParam Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        CommentUpdateResponseDto responseComment = commentService.updateComment(commentId, requestDto, userDto);
        return EntityResponser.responseEntity(HttpStatus.OK, responseComment);
    }

    /**
     * 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글 ID
     * @return HTTP 상태 코드를 포함한 ResponseEntity
     */
    @DeleteMapping
    public ResponseEntity<CommentDeleteResponseDto> deleteComment(
            @RequestParam Long commentId,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        CommentDeleteResponseDto responseComment = commentService.deleteComment(commentId, userDto);
        return EntityResponser.responseEntity(HttpStatus.OK, responseComment);
    }
}
