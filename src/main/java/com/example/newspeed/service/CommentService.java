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
import com.example.newspeed.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UsersRepository userRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * 댓글 생성
     *
     * @param postId        댓글이 등록될 게시글
     * @param requestDto    요청 DTO
     * @return {@link CommentCreateResponseDtoComment} 반환 DTO
     */
    @Transactional
    public CommentCreateResponseDtoComment createComment(Long postId, CommentCreateRequestDto requestDto) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not Found Post"));

        AuthUserDto authUserDto = getAuthUser();
        User user = userRepository.findById(authUserDto.getId()).orElseThrow(() -> new NotFoundException("Not Found User"));

        Comment comment = new Comment(requestDto.getContent(), findPost, user);
        commentRepository.save(comment);

        CommentCreateResponseDtoComment responseDto = modelMapper.map(comment, CommentCreateResponseDtoComment.class);
        return responseDto;
    }

    /**
     * 게시글의 모든 댓글 조회
     *
     * @param postId 조회할 게시글 ID
     * @return {@link CommentFindResponseDtoComment} 반환 DTO 리스트
     */
    public List<CommentFindResponseDtoComment> findCommentByPostId(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not Found Post"));
        List<Comment> comments = findPost.getComments();

        List<CommentFindResponseDtoComment> responseDtoList = comments
                .stream()
                .map(comment -> modelMapper.map(comment, CommentFindResponseDtoComment.class))
                .toList();
        return responseDtoList;
    }

    /**
     * 댓글 수정
     *
     * @param commentId     수정할 댓글 ID
     * @param requestDto    {@link CommentUpdateRequestDto} 요청 DTO
     * @return {@link CommentUpdateResponseDtoComment} 반환 DTO
     */
    //TODO 회원 기능 구현 후 이름 적용 필요
    public CommentUpdateResponseDtoComment updateComment(Long commentId, CommentUpdateRequestDto requestDto) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Not Found Comment"));
        verifyWriterAuthorities(findComment);

        String prevContent = findComment.getContent();
        if(prevContent.equals(requestDto.getContent()))
            throw new IllegalArgumentException("변경할 내용이 없습니다.");

        findComment.updateContent(requestDto.getContent());
        commentRepository.save(findComment);

        CommentUpdateResponseDtoComment responseDto = modelMapper.map(findComment, CommentUpdateResponseDtoComment.class);
        responseDto.setPrevContent(prevContent);

        return responseDto;
    }

    public CommentRemoveResponseDtoComment deleteComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Not Found Comment"));
        verifyWriterAuthorities(findComment);

        CommentRemoveResponseDtoComment responseDto = modelMapper.map(findComment, CommentRemoveResponseDtoComment.class);
        commentRepository.delete(findComment);

        return responseDto;
    }

    private void verifyWriterAuthorities(Comment comment) {
        Optional<Authentication> authentication = Optional.ofNullable(
                SecurityContextHolder.getContext().getAuthentication()
        );
        if(authentication.isPresent() && authentication.get().getPrincipal() instanceof AuthUserDto) {
            AuthUserDto auth = (AuthUserDto) authentication.get().getPrincipal();
            if(!auth.getEmail().equals(comment.getUser().getEmail())) {
                throw new AuthenticationException("Unauthorized");
            }
        } else {
            throw new AuthenticationException("Unauthorized");
        }
    }

    private AuthUserDto getAuthUser() {
        Optional<Authentication> authentication = Optional.ofNullable(
                SecurityContextHolder.getContext().getAuthentication()
        );
        if(authentication.isPresent() && authentication.get().getPrincipal() instanceof AuthUserDto) {
            return (AuthUserDto) authentication.get().getPrincipal();
        } else  {
            throw new AuthenticationException("Unauthorized");
        }
    }
}
