package com.example.newspeed.service;

import com.example.newspeed.dto.comment.*;
import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.Post;
import com.example.newspeed.exception.exceptions.NotFoundException;
import com.example.newspeed.repository.CommentRepository;
import com.example.newspeed.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 댓글 서비스
 */
//TODO 게시글 기능 작성되면 게시글 조회 레포지토리 변경 필요
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * 댓글 생성
     *
     * @param postId        댓글이 등록될 게시글
     * @param requestDto    요청 DTO
     * @return {@link CommentCreateResponseDto} 반환 DTO
     */
    // TODO 회원, 게시글 기능 추가 시 회원, 게시글 정보 추가
    @Transactional
    public CommentCreateResponseDto createComment(Long postId, CommentCreateRequestDto requestDto) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not Found Post"));

        Comment comment = new Comment(requestDto.getContent(), findPost);
        commentRepository.save(comment);

        CommentCreateResponseDto responseDto = modelMapper.map(comment, CommentCreateResponseDto.class);
        return responseDto;
    }

    /**
     * 게시글의 모든 댓글 조회
     *
     * @param postId 조회할 게시글 ID
     * @return {@link CommentFindResponseDto} 반환 DTO 리스트
     */
    public List<CommentFindResponseDto> findCommentByPostId(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not Found Post"));
        List<Comment> comments = findPost.getComments();

        List<CommentFindResponseDto> responseDtoList = comments
                .stream()
                .map(comment -> modelMapper.map(comment, CommentFindResponseDto.class))
                .toList();
        return responseDtoList;
    }

    /**
     * 댓글 수정
     *
     * @param commentId     수정할 댓글 ID
     * @param requestDto    {@link CommentUpdateRequestDto} 요청 DTO
     * @return {@link CommentUpdateResponseDto} 반환 DTO
     */
    //TODO 회원 기능 구현 후 이름 적용 필요
    public CommentUpdateResponseDto updateComment(Long commentId, CommentUpdateRequestDto requestDto) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Not Found Comment"));
        String prevContent = findComment.getContent();

        if(prevContent.equals(requestDto.getContent()))
            throw new IllegalArgumentException("변경할 내용이 없습니다.");

        findComment.updateContent(requestDto.getContent());
        commentRepository.save(findComment);

        CommentUpdateResponseDto responseDto = modelMapper.map(findComment, CommentUpdateResponseDto.class);
        responseDto.setPrevContent(prevContent);

        return responseDto;
    }

    public CommentRemoveResponseDto deleteComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Not Found Comment"));

        CommentRemoveResponseDto responseDto = modelMapper.map(findComment, CommentRemoveResponseDto.class);
        commentRepository.delete(findComment);

        return responseDto;
    }
}
