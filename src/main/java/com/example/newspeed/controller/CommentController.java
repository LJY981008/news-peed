package com.example.newspeed.controller;

import com.example.newspeed.dto.comment.CommentCreateRequestDto;
import com.example.newspeed.dto.comment.CommentCreateResponseDto;
import com.example.newspeed.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param postId
     * @param requestDto
     */
    @PostMapping
    public ResponseEntity<CommentCreateResponseDto> createComment(
            @RequestParam Long postId,
            @RequestBody CommentCreateRequestDto requestDto
    ) {
        CommentCreateResponseDto commentResponse = commentService.createComment(postId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponse);
    }
}
