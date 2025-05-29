package com.example.newspeed.service;

import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.dto.comment.*;
import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.Post;
import com.example.newspeed.entity.User;
import com.example.newspeed.exception.exceptions.AuthenticationException;
import com.example.newspeed.exception.exceptions.NotFoundException;
import com.example.newspeed.repository.CommentRepository;
import com.example.newspeed.repository.PostRepository;
import com.example.newspeed.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 댓글 서비스
 */
//TODO 게시글 기능 작성되면 게시글 조회 레포지토리 변경 필요
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /**
     * 댓글 생성
     *
     * @param postId        댓글이 등록될 게시글
     * @param requestDto    요청 DTO
     * @return {@link CommentCreateResponseDto} 반환 DTO
     */
    @Transactional
    public CommentCreateResponseDto createComment(Long postId, CommentCreateRequestDto requestDto, AuthUserDto userDto) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not Found Post"));
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new NotFoundException("Not Found User"));

        Comment comment = new Comment(requestDto.getContent(), findPost, user);
        commentRepository.save(comment);

        CommentCreateResponseDto responseDto = new CommentCreateResponseDto(comment);
        return responseDto;
    }

    /**
     * 게시글의 모든 댓글 조회
     *
     * @param postId 조회할 게시글 ID
     * @return {@link CommentFindResponseDto} 반환 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<CommentFindResponseDto> findCommentByPostId(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not Found Post"));
        List<Comment> comments = findPost.getComments();

        List<CommentFindResponseDto> responseDtoList = comments
                .stream()
                .map(CommentFindResponseDto::new)
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
    @Transactional
    public CommentUpdateResponseDto updateComment(Long commentId, CommentUpdateRequestDto requestDto, AuthUserDto userDto) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Not Found Comment"));
        verifyWriterAuthorities(findComment, userDto);

        String prevContent = findComment.getContent();
        if(prevContent.equals(requestDto.getContent()))
            throw new IllegalArgumentException("변경할 내용이 없습니다.");

        findComment.updateContent(requestDto.getContent());
        commentRepository.save(findComment);

        CommentUpdateResponseDto responseDto = new CommentUpdateResponseDto(findComment);
        responseDto.setPrevContent(prevContent);

        return responseDto;
    }

    @Transactional
    public CommentRemoveResponseDto deleteComment(Long commentId, AuthUserDto userDto) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Not Found Comment"));
        verifyWriterAuthorities(findComment, userDto);

        CommentRemoveResponseDto responseDto = new  CommentRemoveResponseDto(findComment);
        commentRepository.delete(findComment);

        return responseDto;
    }

    private void verifyWriterAuthorities(Comment comment, AuthUserDto userDto) {
        if(!userDto.getEmail().equals(comment.getUser().getEmail())) {
            throw new AuthenticationException("Unauthorized");
        }
    }
}
