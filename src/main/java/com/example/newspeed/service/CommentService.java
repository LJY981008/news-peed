package com.example.newspeed.service;

import com.example.newspeed.constant.ExceptionMessage;
import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.dto.comment.*;
import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.Post;
import com.example.newspeed.entity.User;
import com.example.newspeed.exception.exceptions.AuthenticationException;
import com.example.newspeed.exception.exceptions.InvalidRequestException;
import com.example.newspeed.exception.exceptions.NotFoundException;
import com.example.newspeed.repository.Comment.CommentRepository;
import com.example.newspeed.repository.PostRepository;
import com.example.newspeed.repository.UserRepository;
import com.example.newspeed.util.CommentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 댓글 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 댓글의 생성, 조회, 수정, 삭제 기능을 제공합니다.
 *
 * @author 이준영
 */
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
     * 새로운 댓글을 생성합니다.
     *
     * @param postId 댓글이 등록될 게시글 ID
     * @param requestDto 댓글 생성 요청 DTO
     * @param userDto 로그인된 사용자 정보
     * @return 생성된 댓글 정보
     * @throws NotFoundException 게시글이나 사용자를 찾을 수 없는 경우
     */
    @Transactional
    public CommentCreateResponseDto createComment(
            Long postId,
            CommentCreateRequestDto requestDto,
            AuthUserDto userDto
    ) {
        Post findPost = postRepository.findPostByPostIdAndDeletedFalse(postId).orElseThrow(() -> new NotFoundException(ExceptionMessage.POST_NOT_FOUND));
        User user = userRepository.findByEmailAndDeletedFalse(userDto.getEmail()).orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));

        Comment comment = new Comment(requestDto.getContent(), findPost, user);
        commentRepository.save(comment);

        return CommentMapper.toDto(comment, CommentCreateResponseDto.class);
    }

    /**
     * 특정 게시글의 모든 댓글을 조회합니다.
     *
     * @param postId 조회할 게시글 ID
     * @return 게시글의 댓글 목록
     */
    @Transactional(readOnly = true)
    public List<CommentFindResponseDto> findCommentByPostId(Long postId) {
        List<Comment> comments = commentRepository.findCommentByPostId(postId);
        return comments
                .stream()
                .map(comment -> CommentMapper.toDto(comment, CommentFindResponseDto.class))
                .toList();
    }

    /**
     * 댓글을 수정합니다.
     *
     * @param commentId 수정할 댓글 ID
     * @param requestDto 수정할 댓글 내용
     * @param userDto 로그인된 사용자 정보
     * @return 수정된 댓글 정보 (이전 내용 포함)
     * @throws NotFoundException 댓글을 찾을 수 없는 경우
     * @throws AuthenticationException 수정 권한이 없는 경우
     * @throws InvalidRequestException 이전 내용과 동일한 경우
     */
    @Transactional
    public CommentUpdateResponseDto updateComment(
            Long commentId,
            CommentUpdateRequestDto requestDto,
            AuthUserDto userDto
    ) {
        Comment findComment = getCommentById(commentId);
        verifyWriterAuthorities(findComment, userDto);

        String prevContent = findComment.getContent();
        if (prevContent.equals(requestDto.getContent()))
            throw new InvalidRequestException(ExceptionMessage.SAME_CONTENT);

        findComment.updateContent(requestDto.getContent());
        commentRepository.save(findComment);

        CommentUpdateResponseDto responseDto = CommentMapper.toDto(findComment, CommentUpdateResponseDto.class);
        responseDto.setPrevContent(prevContent);

        return responseDto;
    }

    /**
     * 댓글을 삭제합니다 (소프트 삭제).
     *
     * @param commentId 삭제할 댓글 ID
     * @param userDto 로그인된 사용자 정보
     * @return 삭제된 댓글 정보
     * @throws NotFoundException 댓글을 찾을 수 없는 경우
     * @throws AuthenticationException 삭제 권한이 없는 경우
     */
    @Transactional
    public CommentDeleteResponseDto deleteComment(Long commentId, AuthUserDto userDto) {
        Comment findComment = getCommentById(commentId);
        verifyWriterAuthorities(findComment, userDto);

        findComment.softDelete();
        commentRepository.save(findComment);

        return CommentMapper.toDto(findComment, CommentDeleteResponseDto.class);
    }

    /**
     * 댓글 ID로 댓글을 조회합니다.
     *
     * @param commentId 조회할 댓글 ID
     * @return 조회된 댓글
     * @throws NotFoundException 댓글을 찾을 수 없는 경우
     */
    private Comment getCommentById(Long commentId) {
        return commentRepository.findCommentByCommentId(commentId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.COMMENT_NOT_FOUND));
    }

    /**
     * 댓글 작성자 권한을 검증합니다.
     *
     * @param comment 검증할 댓글
     * @param userDto 로그인된 사용자 정보
     * @throws AuthenticationException 댓글 작성자가 아닌 경우
     */
    private void verifyWriterAuthorities(Comment comment, AuthUserDto userDto) {
        if (!userDto.getEmail().equals(comment.getUser().getEmail())) {
            throw new AuthenticationException(ExceptionMessage.UNAUTHORIZED);
        }
    }
}
