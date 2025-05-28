package com.example.newspeed.service;

import com.example.newspeed.dto.comment.CommentCreateRequestDto;
import com.example.newspeed.dto.comment.CommentCreateResponseDto;
import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.Post;
import com.example.newspeed.repository.CommentRepository;

import java.util.Optional;

public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    //TODO 커스텀 예외 선언 필요, 회원, 게시글 기능 추가 시 회원, 게시글 정보 추가
    public CommentCreateResponseDto createComment(Long postId, CommentCreateRequestDto requestDto) {
        Optional<Post> findPost = commentRepository.findPostById(postId);
        if(findPost.isEmpty()) throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        Post post = findPost.get();

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);

        CommentCreateResponseDto responseDto = CommentCreateResponseDto.builder()
                .id(comment.getId())
                .postId(post.getId())
                .build();
        return responseDto;
    }
}
