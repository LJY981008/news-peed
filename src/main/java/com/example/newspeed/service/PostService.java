package com.example.newspeed.service;


import com.example.newspeed.dto.post.*;

import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.Post;
import com.example.newspeed.entity.User;
import com.example.newspeed.entity.PostLike;
import com.example.newspeed.enums.UserRole;
import com.example.newspeed.exception.exceptions.NotFoundException;
import com.example.newspeed.repository.follow.FollowRepository;
import com.example.newspeed.repository.like.LikeRepository;
import com.example.newspeed.repository.PostRepository;
import com.example.newspeed.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    // 게시글 전체
    @Transactional
    public Page<FindPostResponseDto> findAllPost(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByDeletedFalse(pageable);
        if (posts.isEmpty()) {
            throw new NotFoundException("해당 페이지는 존재 하지 않습니다."); // 페이지는 0부터 시작합니다.
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
    // 해당 날짜의 게시글 전체 조회
    @Transactional
    public Page<FindPostResponseDto> findAllByDate(LocalDate createdAt, Pageable pageable) {
        LocalDateTime startTime = createdAt.atStartOfDay();
        LocalDateTime endTime = createdAt.plusDays(1).atStartOfDay();
        Page<Post> posts = postRepository.findAllByCreatedAtBetweenAndDeletedFalse(startTime, endTime, pageable);
        if (posts.isEmpty()) {
            throw new NotFoundException(createdAt + " 날짜에 만들어진 게시물이 없습니다.");
        }
        return posts.map(FindPostResponseDto::findPostDto);
    }

    /**
     * @author 김태현
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글 정보를 담은 DTO (FindPostResponseDto)
     * @throws NotFoundException 해당 ID의 게시글이 존재하지 않거나 삭제된 경우
     */
    // 게시글 단건 조회
    @Transactional
    public FindPostResponseDto findById(Long postId) {
        Post findPost = postRepository.findPostByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new NotFoundException("존재 하지 않는 게시글 입니다."));
        return new FindPostResponseDto(findPost);
    }


    @Transactional
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
        Long userId = userDto.getId();
        User user = usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("로그인이 필요한 서비스입니다."));
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
        Post post = postRepository.findPostByPostIdAndDeletedFalse(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "찾을 수 없는 게시물 입니다."));

        String msg = verifyWriterAuthorities(post, authUserDto);

        post.softDelete();
        logicalDeleteCascade(post);
        postRepository.save(post);
        return new DeletePostResponseDto(msg, "/post/find-all");
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
    // 게시글 수정
    @Transactional
    public FindPostResponseDto updatePost(Long postId, AuthUserDto authUserDto, UpdatePostRequestDto updateDto) {
        Post findPost = postRepository.findPostByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new NotFoundException("수정할 게시글을 찾지 못했습니다."));

        String msg = verifyWriterAuthorities(findPost, authUserDto);

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

        Optional<PostLike> postLike = likeRepository.findByPostIdAndUserId(postId, authUserDto.getId());
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not Found Post"));

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

        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not Found Post"));
        return new GetLikeResponseDto(post);
    }

    /**
     * <p>좋아요 테이블 생성<p/>
     *
     * @param postId      좋아요에 해당하는 게시글 Index
     * @param authUserDto 로그인된 사용자 정보
     * @author 이준영
     */
    private void createPostLike(Long postId, AuthUserDto authUserDto) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not Found Post"));
        User user = userRepository.findById(authUserDto.getId()).orElseThrow(() -> new NotFoundException("Not Found User"));
        PostLike postLike = new PostLike(user, post);
        likeRepository.save(postLike);
    }

    /**
     * <p>좋아요 테이블 삭제</p>
     *
     * @param postLike 좋아요 테이블
     * @author 이준영
     */
    private void deletePostLike(PostLike postLike) {
        likeRepository.delete(postLike);
    }

    private void logicalDeleteCascade(Post post) {
        List<Comment> comments = post.getComments();
        comments.forEach(Comment::softDelete);
    }


    private String verifyWriterAuthorities(Post post, AuthUserDto authUserDto) {
        Long loginUserId = authUserDto.getId();
        UserRole loginUserRole = authUserDto.getUserRole();
        String msg = "";

        if (loginUserRole == UserRole.ADMIN) {
            msg = "관리자 권한";
        } else if (post.getUser().getUserId().equals(loginUserId)) {
            msg = "정상적";
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다");
        }

        return msg;
    }
}
