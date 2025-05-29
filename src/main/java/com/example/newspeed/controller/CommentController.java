package com.example.newspeed.controller;

import com.example.newspeed.dto.comment.*;
import com.example.newspeed.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 댓글 생성
     *
     * @param postId        댓글이 등록될 포스트의 ID
     * @param requestDto    {@link CommentCreateRequestDto} 요청 DTO
     * @return {@link CommentCreateResponseDtoComment} 반환 DTO
     */
    @PostMapping
    public ResponseEntity<CommentCreateResponseDtoComment> createComment(
            @RequestParam Long postId,
            @Valid @RequestBody CommentCreateRequestDto requestDto
    ) {
        CommentCreateResponseDtoComment responseComment = commentService.createComment(postId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseComment);
    }

    /**
     * 댓글 조회
     *
     * @param postId    댓글을 조회할 포스트의 ID
     * @return {@link CommentFindResponseDtoComment} 반환 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<List<CommentFindResponseDtoComment>> findCommentByPostId(
            @RequestParam Long postId
    ) {
        List<CommentFindResponseDtoComment> responseComments = commentService.findCommentByPostId(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseComments);
    }

    @PatchMapping
    public ResponseEntity<CommentUpdateResponseDtoComment> updateComment(
            @RequestParam Long commentId,
            @Valid @RequestParam CommentUpdateRequestDto requestDto
    ) {
        CommentUpdateResponseDtoComment responseComment = commentService.updateComment(commentId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseComment);
    }

    @DeleteMapping
    public ResponseEntity<CommentRemoveResponseDtoComment> deleteComment(
            @RequestParam Long commentId
    ){
        CommentRemoveResponseDtoComment responseComment = commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(responseComment);
    }
}
