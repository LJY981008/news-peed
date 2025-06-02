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

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository usersRepository;
    private final FollowRepository followRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 조회기능
     *
     * @param pageable 페이징 및 정렬 정보
     * @return 삭제되지 않은 게시글 목록을 담은 Page 객체 (Page<FindPostResponseDto>)
     * @throws NotFoundException 해당 페이지에 게시글이 존재하지 않는 경우
     * @author 김태현
     */
    @Transactional(readOnly = true)
    public Page<FindPostResponseDto> findAllPost(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByDeletedFalse(pageable);
        if (posts.isEmpty()) {
            throw new NotFoundException(ExceptionMessage.PAGE_NOT_FOUND);
        }
        return posts.map(FindPostResponseDto::findPostDto);
    }

    /**
     * 해당 날짜에 생성된 게시글 전체 조회
     *
     * @author 김태현
     * @param createdAt 조회할 날짜 (예: 2025-06-01). 하루 단위로 조회됩니다.
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징된 게시글 목록 (Page<FindPostResponseDto>)
     * @throws NotFoundException 해당 날짜에 생성된 게시글이 하나도 없는 경우
     */
    @Transactional(readOnly = true)
    public Page<FindPostResponseDto> findAllByDate(LocalDate createdAt, Pageable pageable) {
        LocalDateTime startTime = createdAt.atStartOfDay();
        LocalDateTime endTime = createdAt.plusDays(1).atStartOfDay();
        Page<Post> posts = postRepository.findAllByCreatedAtBetweenAndDeletedFalse(startTime, endTime, pageable);
        if (posts.isEmpty()) {
            throw new NotFoundException(String.format(ExceptionMessage.DATE_POST_NOT_FOUND, createdAt));
        }
        return posts.map(FindPostResponseDto::findPostDto);
    }

    /**
     * @author 김태현
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글 정보를 담은 DTO (FindPostResponseDto)
     * @throws NotFoundException 해당 ID의 게시글이 존재하지 않거나 삭제된 경우
     */
    @Transactional(readOnly = true)
    public FindPostResponseDto findById(Long postId) {
        Post findPost = findPostById(postId);
        return new FindPostResponseDto(findPost);
    }

    @Transactional(readOnly = true)
    public Page<FindPostResponseDto> findFollowingPosts(Long currentUserId, Pageable pageable) {
        List<Long> followedUserIds = followRepository.findFollowedUserIdsByFollowingUserId(currentUserId);
        return postRepository.findByUser_UserIdInAndDeletedFalse(followedUserIds, pageable)
                .map(FindPostResponseDto::findPostDto);
    }

    /**
     * <p>게시글 생성 메서드</p>
     *
     * @param title    게시글 제목
     * @param content  게시글 내용
     * @param imageUrl 게시글 이미지
     * @return 성공시 반환되는 {@link CreatePostResponseDto} 성공메세지와 게시글 URL
     * @author 윤희준
     */
    @Transactional
    public CreatePostResponseDto createPost(String title, String content, String imageUrl, AuthUserDto userDto) {
        User user = findUserById(userDto.getId());
        Post post = new Post(title, content, imageUrl, user);
        postRepository.save(post);
        return new CreatePostResponseDto("게시글 생성에 성공했습니다", "/post/find-all");
    }

    /**
     * <p>게시글 삭제 메서드</p>
     *
     * @param postId      게시글의 pk값
     * @param authUserDto 인증된 사용자 정보를 담은 {@link AuthUserDto}
     * @return 게시글의 존재여부, 사용자의 권한여부 비교후 삭제진행
     * @author 윤희준
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
     * 게시글 수정 기능
     *
     * @param postId      수정할 게시글의 ID
     * @param authUserDto 로그인한 사용자 정보 (작성자 권한 확인용)
     * @param updateDto   수정할 게시글의 제목과 내용이 담긴 DTO
     * @return 수정된 게시글 정보를 담은 DTO (FindPostResponseDto)
     * @throws NotFoundException 게시글이 존재하지 않거나 삭제된 경우
     * @author 김태현
     */
    @Transactional
    public FindPostResponseDto updatePost(Long postId, AuthUserDto authUserDto, UpdatePostRequestDto updateDto) {
        Post findPost = findPostById(postId);
        verifyWriterAuthorities(findPost, authUserDto);

        if (findPost.getContent().equals(updateDto.getContents())) {
            throw new InvalidRequestException("이전 내용과 동일한 내용입니다.");
        }

        findPost.updatePost(updateDto);
        return new FindPostResponseDto(findPost);
    }

    /**
     * <p>좋아요 작동 이벤트</p>
     * <p>한번 누르면 반대되는 값으로 변경하는 토글방식</p>
     *
     * @param postId      좋아요에 해당하는 게시글 Index
     * @param authUserDto 로그인된 사용자 정보
     * @author 이준영
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
            PostLike postLikeEntity = postLike.get();
            likeRepository.updateLikeStatus(post, false);
            deletePostLike(postLikeEntity);
        } else {
            likeRepository.updateLikeStatus(post, true);
            createPostLike(postId, authUserDto);
        }
    }

    /**
     * <p>게시글의 좋아요 개수 조회</p>
     *
     * @param postId 좋아요에 해당하는 게시글의 Index
     * @return {@link GetLikeResponseDto}
     * @author 이준영
     */
    @Transactional(readOnly = true)
    public GetLikeResponseDto getPostLike(Long postId) {
        Post post = findPostById(postId);
        return new GetLikeResponseDto(post);
    }

    private Post findPostById(Long postId) {
        return postRepository.findPostByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.POST_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));
    }

    private void createPostLike(Long postId, AuthUserDto authUserDto) {
        Post post = findPostById(postId);
        User user = findUserById(authUserDto.getId());
        PostLike postLike = new PostLike(user, post);
        likeRepository.save(postLike);
    }

    private void deletePostLike(PostLike postLike) {
        likeRepository.delete(postLike);
    }

    private void logicalDeleteCascade(Post post) {
        List<Comment> comments = post.getComments();
        comments.forEach(Comment::softDelete);
    }

    private void verifyWriterAuthorities(Post post, AuthUserDto authUserDto) {
        Long loginUserId = authUserDto.getId();
        UserRole loginUserRole = authUserDto.getUserRole();

        if (loginUserRole != UserRole.ADMIN && !post.getUser().getUserId().equals(loginUserId)) {
            throw new AuthenticationException(ExceptionMessage.UNAUTHORIZED);
        }
    }
}
