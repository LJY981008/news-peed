package com.example.newspeed.service;

import com.example.newspeed.constant.ExceptionMessage;
import com.example.newspeed.dto.post.*;
import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.Post;
import com.example.newspeed.entity.User;
import com.example.newspeed.entity.PostLike;
import com.example.newspeed.enums.UserRole;
import com.example.newspeed.exception.exceptions.NotFoundException;
import com.example.newspeed.exception.exceptions.AuthenticationException;
import com.example.newspeed.exception.exceptions.InvalidRequestException;
import com.example.newspeed.repository.follow.FollowRepository;
import com.example.newspeed.repository.like.LikeRepository;
import com.example.newspeed.repository.PostRepository;
import com.example.newspeed.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 게시글 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 
 * @author 김태현, 윤희준, 이준영
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository usersRepository;
    private final FollowRepository followRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    /**
     * 모든 게시글을 페이징하여 조회
     *
     * @param pageable 페이징 및 정렬 정보
     * @return 삭제되지 않은 게시글 목록을 담은 Page 객체
     * @throws NotFoundException 해당 페이지에 게시글이 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public Page<FindPostResponseDto> findAllPost(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByDeletedFalse(pageable);
        validatePageNotEmpty(posts);
        return posts.map(FindPostResponseDto::findPostDto);
    }

    /**
     * 특정 날짜에 작성된 게시글을 페이징하여 조회
     *
     * @param createdAt 조회할 날짜 (예: 2025-06-01)
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징된 게시글 목록
     * @throws NotFoundException 해당 날짜에 작성된 게시글이 없는 경우
     */
    @Transactional(readOnly = true)
    public Page<FindPostResponseDto> findAllByDate(LocalDate createdAt, Pageable pageable) {
        LocalDateTime startTime = createdAt.atStartOfDay();
        LocalDateTime endTime = createdAt.plusDays(1).atStartOfDay();
        Page<Post> posts = postRepository.findAllByCreatedAtBetweenAndDeletedFalse(startTime, endTime, pageable);
        validatePageNotEmpty(posts);
        return posts.map(FindPostResponseDto::findPostDto);
    }

    /**
     * 특정 게시글 상세 조회
     *
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글 정보
     * @throws NotFoundException 해당 ID의 게시글이 존재하지 않거나 삭제된 경우
     */
    @Transactional(readOnly = true)
    public FindPostResponseDto findById(Long postId) {
        Post findPost = findPostById(postId);
        return new FindPostResponseDto(findPost);
    }

    /**
     * 팔로우한 사용자들의 게시글을 페이징하여 조회
     *
     * @param currentUserId 현재 로그인한 사용자 ID
     * @param pageable 페이징 및 정렬 정보
     * @return 팔로우한 사용자들의 게시글 목록
     */
    @Transactional(readOnly = true)
    public Page<FindPostResponseDto> findFollowingPosts(Long currentUserId, Pageable pageable) {
        List<Long> followedUserIds = followRepository.findFollowedUserIdsByFollowingUserId(currentUserId);
        return postRepository.findByUser_UserIdInAndDeletedFalse(followedUserIds, pageable)
                .map(FindPostResponseDto::findPostDto);
    }

    /**
     * 새로운 게시글 생성
     *
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @param imageUrl 게시글 이미지 URL
     * @param userDto 작성자 정보
     * @return 생성된 게시글 정보와 성공 메시지
     */
    @Transactional
    public CreatePostResponseDto createPost(String title, String content, String imageUrl, AuthUserDto userDto) {
        User user = findUserById(userDto.getId());
        Post post = new Post(title, content, imageUrl, user);
        postRepository.save(post);
        return new CreatePostResponseDto("게시글 생성에 성공했습니다", "/post/find-all");
    }

    /**
     * 게시글 삭제 (소프트 삭제)
     *
     * @param postId 삭제할 게시글 ID
     * @param authUserDto 인증된 사용자 정보
     * @return 삭제 결과 메시지
     * @throws NotFoundException 게시글이 존재하지 않는 경우
     * @throws AuthenticationException 삭제 권한이 없는 경우
     */
    @Transactional
    public DeletePostResponseDto deletePost(Long postId, AuthUserDto authUserDto) {
        Post post = findPostById(postId);
        verifyWriterAuthorities(post, authUserDto);

        post.softDelete();
        logicalDeleteCascade(post);
        postRepository.save(post);
        return new DeletePostResponseDto(ExceptionMessage.NORMAL_AUTHORITY, "/post/find-all");
    }

    /**
     * 게시글 수정
     *
     * @param postId 수정할 게시글 ID
     * @param authUserDto 인증된 사용자 정보
     * @param updateDto 수정할 내용
     * @return 수정된 게시글 정보
     * @throws NotFoundException 게시글이 존재하지 않는 경우
     * @throws AuthenticationException 수정 권한이 없는 경우
     * @throws InvalidRequestException 이전 내용과 동일한 경우
     */
    @Transactional
    public FindPostResponseDto updatePost(Long postId, AuthUserDto authUserDto, UpdatePostRequestDto updateDto) {
        Post findPost = findPostById(postId);
        verifyWriterAuthorities(findPost, authUserDto);

        validateContentNotSame(findPost, updateDto);
        findPost.updatePost(updateDto);
        return new FindPostResponseDto(findPost);
    }

    /**
     * 게시글 좋아요 토글
     * 좋아요가 없는 경우 생성하고, 있는 경우 삭제
     *
     * @param postId 게시글 ID
     * @param authUserDto 인증된 사용자 정보
     * @throws OptimisticLockingFailureException 동시성 문제 발생 시
     */
    @Retryable(
            value = OptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public void toggleLike(Long postId, AuthUserDto authUserDto) {
        Post post = findPostById(postId);
        Optional<PostLike> postLike = likeRepository.findByPostIdAndUserId(postId, authUserDto.getId());

        if (postLike.isPresent()) {
            handleExistingLike(post, postLike.get());
        } else {
            handleNewLike(post, postId, authUserDto);
        }
    }

    /**
     * 게시글의 좋아요 수 조회
     *
     * @param postId 게시글 ID
     * @return 좋아요 정보
     * @throws NotFoundException 게시글이 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public GetLikeResponseDto getPostLike(Long postId) {
        Post post = findPostById(postId);
        return new GetLikeResponseDto(post);
    }

    /**
     * 게시글 ID로 게시글 조회
     *
     * @param postId 게시글 ID
     * @return 조회된 게시글
     * @throws NotFoundException 게시글이 존재하지 않는 경우
     */
    private Post findPostById(Long postId) {
        return postRepository.findPostByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.POST_NOT_FOUND));
    }

    /**
     * 사용자 ID로 사용자 조회
     *
     * @param userId 사용자 ID
     * @return 조회된 사용자
     * @throws NotFoundException 사용자가 존재하지 않는 경우
     */
    private User findUserById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));
    }

    /**
     * 페이지가 비어있는지 검증
     *
     * @param posts 검증할 게시글 페이지
     * @throws NotFoundException 페이지가 비어있는 경우
     */
    private void validatePageNotEmpty(Page<Post> posts) {
        if (posts.isEmpty()) {
            throw new NotFoundException(ExceptionMessage.PAGE_NOT_FOUND);
        }
    }

    /**
     * 게시글 내용이 이전과 동일한지 검증
     *
     * @param post 기존 게시글
     * @param updateDto 수정할 내용
     * @throws InvalidRequestException 내용이 동일한 경우
     */
    private void validateContentNotSame(Post post, UpdatePostRequestDto updateDto) {
        if (post.getContent().equals(updateDto.getContents())) {
            throw new InvalidRequestException(ExceptionMessage.SAME_CONTENT);
        }
    }

    /**
     * 기존 좋아요 처리
     *
     * @param post 게시글
     * @param postLike 좋아요 정보
     */
    private void handleExistingLike(Post post, PostLike postLike) {
        likeRepository.updateLikeStatus(post, false);
        deletePostLike(postLike);
    }

    /**
     * 새로운 좋아요 처리
     *
     * @param post 게시글
     * @param postId 게시글 ID
     * @param authUserDto 인증된 사용자 정보
     */
    private void handleNewLike(Post post, Long postId, AuthUserDto authUserDto) {
        likeRepository.updateLikeStatus(post, true);
        createPostLike(postId, authUserDto);
    }

    /**
     * 새로운 좋아요 생성
     *
     * @param postId 게시글 ID
     * @param authUserDto 인증된 사용자 정보
     */
    private void createPostLike(Long postId, AuthUserDto authUserDto) {
        Post post = findPostById(postId);
        User user = findUserById(authUserDto.getId());
        PostLike postLike = new PostLike(user, post);
        likeRepository.save(postLike);
    }

    /**
     * 좋아요 삭제
     *
     * @param postLike 삭제할 좋아요 정보
     */
    private void deletePostLike(PostLike postLike) {
        likeRepository.delete(postLike);
    }

    /**
     * 게시글 삭제 시 연관된 댓글도 함께 삭제
     *
     * @param post 삭제할 게시글
     */
    private void logicalDeleteCascade(Post post) {
        List<Comment> comments = post.getComments();
        comments.forEach(Comment::softDelete);
    }

    /**
     * 게시글 작성자 권한 검증
     *
     * @param post 게시글
     * @param authUserDto 인증된 사용자 정보
     * @throws AuthenticationException 권한이 없는 경우
     */
    private void verifyWriterAuthorities(Post post, AuthUserDto authUserDto) {
        Long loginUserId = authUserDto.getId();
        UserRole loginUserRole = authUserDto.getUserRole();

        if (loginUserRole != UserRole.ADMIN && !post.getUser().getUserId().equals(loginUserId)) {
            throw new AuthenticationException(ExceptionMessage.UNAUTHORIZED);
        }
    }
}
