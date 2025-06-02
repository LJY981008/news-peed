package com.example.newspeed.service;

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
 * 댓글 서비스
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
     * <p>댓글 생성</p>
     *
     * @param postId     댓글이 등록될 게시글
     * @param requestDto 요청 DTO
     * @param userDto    로그인된 사용자 정보
     * @return {@link CommentCreateResponseDto}
     */
    @Transactional
    public CommentCreateResponseDto createComment(
            Long postId,
            CommentCreateRequestDto requestDto,
            AuthUserDto userDto
    ) {
        Post findPost = postRepository.findPostByPostIdAndDeletedFalse(postId).orElseThrow(() -> new NotFoundException("Not Found Post"));
        User user = userRepository.findByEmailAndDeletedFalse(userDto.getEmail()).orElseThrow(() -> new NotFoundException("Not Found User"));

        Comment comment = new Comment(requestDto.getContent(), findPost, user);
        commentRepository.save(comment);

        return CommentMapper.toDto(comment, CommentCreateResponseDto.class);
    }

    /**
     * <p>게시글의 모든 댓글 조회</p>
     *
     * @param postId 조회할 게시글 ID
     * @return {@link CommentFindResponseDto} 리스트
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
     * <p>댓글 수정</p>
     *
     * @param commentId  수정할 댓글 ID
     * @param requestDto {@link CommentUpdateRequestDto}
     * @param userDto    로그인된 사용자 정보
     * @return {@link CommentUpdateResponseDto}
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
            throw new InvalidRequestException("Same Prev Conmment");

        findComment.updateContent(requestDto.getContent());
        commentRepository.save(findComment);

        CommentUpdateResponseDto responseDto = CommentMapper.toDto(findComment, CommentUpdateResponseDto.class);
        responseDto.setPrevContent(prevContent);

        return responseDto;
    }

    /**
     * <p>댓글 삭제</p>
     *
     * @param commentId 댓글 Index
     * @param userDto   로그인된 사용자 정보
     * @return {@link CommentDeleteResponseDto}
     */
    @Transactional
    public CommentDeleteResponseDto deleteComment(Long commentId, AuthUserDto userDto) {

        Comment findComment = getCommentById(commentId);
        verifyWriterAuthorities(findComment, userDto);

        findComment.softDelete();
        commentRepository.save(findComment);

        return CommentMapper.toDto(findComment, CommentDeleteResponseDto.class);
    }

    // 댓글 단건 조회
    private Comment getCommentById(Long commentId) {
        return commentRepository.findCommentByCommentId(commentId).orElseThrow(() -> new NotFoundException("Not Found Comment"));
    }

    /**
     * <p>로그인된 사용자의 해당 댓글 접근권한 체크</p>
     *
     * @param comment 접근하는 댓글
     * @param userDto 로그인된 사용자 정보
     */
    private void verifyWriterAuthorities(Comment comment, AuthUserDto userDto) {

        if (!userDto.getEmail().equals(comment.getUser().getEmail())) {
            throw new AuthenticationException("Unauthorized");
        }
    }
}
