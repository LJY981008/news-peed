package com.example.newspeed.service;


import com.example.newspeed.dto.post.*;

import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.entity.Post;
import com.example.newspeed.entity.PostLike;
import com.example.newspeed.entity.User;
import com.example.newspeed.enums.UserRole;
import com.example.newspeed.exception.exceptions.NotFoundException;
import com.example.newspeed.repository.FollowRepository;
import com.example.newspeed.repository.LikeRepository;
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

    // 게시글 전체
    @Transactional
    public Page<FindPostResponseDto> findPost(Pageable pageable){
        return postRepository.findAll(pageable).map(FindPostResponseDto::findPostDto);
    }

    @Transactional
    public Page<FindPostResponseDto> findFollowingPosts(Long currentUserId, Pageable pageable){
        List<Long> followedUserIds = followRepository.findFollowedUserIdsByFollowingUserId(currentUserId);

        return postRepository.findByUser_UserIdIn(followedUserIds, pageable)
                .map(FindPostResponseDto::findPostDto);
    }

    // 게시글 단건 조회
    @Transactional
    public FindPostResponseDto findById(Long userId) {
        Post findPost = postRepository.findById(userId).orElseThrow(() -> new NotFoundException("없음"));
        return new FindPostResponseDto(findPost);
    }
    // 게시글 생성
    @Transactional
    public CreatePostResponseDto createPost(String title, String content, String imageUrl, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new NotFoundException("없음"));
        Post post = new Post(title, content, imageUrl, user);
        postRepository.save(post);
        return new CreatePostResponseDto();
    }

    // 게시글 삭제
    @Transactional
    public DeletePostResponseDto deletePost(Long postId, AuthUserDto authUserDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 게시글이 없습니다."));
        Long loginUserId = authUserDto.getId();
        UserRole loginUserRole = authUserDto.getUserRole();

        if ( loginUserRole == UserRole.ADMIN ) {
            postRepository.delete(post);
            return new DeletePostResponseDto("관리자 권한으로 삭제되었습니다.");
        }
        else if (!post.getUser().getUserId().equals(loginUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제할 권한이 없습니다");
        }
        else {
            postRepository.delete(post);
            return new DeletePostResponseDto("정상적으로 삭제되었습니다.");
        }
    }

    // 게시글 수정
    @Transactional
    public FindPostResponseDto updatePost(Long postId, AuthUserDto authUserDto, UpdatePostRequestDto updateDto) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("수정할 게시글을 찾지 못했습니다."));
        Long loginUserId = authUserDto.getId(); // 로그인 유저 id 검사
        if(!findPost.getUser().getUserId().equals(loginUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 수정할 수 있습니다.");
        }
        findPost.updatePost(updateDto);
        return new FindPostResponseDto(findPost);
    }


    /**
     * <p>좋아요 작동 이벤트</p>
     * <p>한번 누르면 반대되는 값으로 변경하는 토글방식</p>
     *
     * @author 이준영
     * @param postId 좋아요에 해당하는 게시글 Index
     * @param authUserDto 로그인된 사용자 정보
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

        if(postLike.isPresent()) {
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
     * @author 이준영
     * @param postId 좋아요에 해당하는 게시글의 Index
     * @return {@link GetLikeResponseDto}
     */
    @Transactional(readOnly = true)
    public GetLikeResponseDto getPostLike(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not Found Post"));
        return new GetLikeResponseDto(post);
    }

    /**
     * <p>좋아요 테이블 생성<p/>
     *
     * @author 이준영
     * @param postId 좋아요에 해당하는 게시글 Index
     * @param authUserDto 로그인된 사용자 정보
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
     * @author 이준영
     * @param postLike 좋아요 테이블
     */
    private void deletePostLike(PostLike postLike) {
        likeRepository.delete(postLike);
    }
}
