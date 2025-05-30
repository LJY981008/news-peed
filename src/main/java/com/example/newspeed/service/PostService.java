package com.example.newspeed.service;


import com.example.newspeed.dto.post.*;

import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.entity.Post;
import com.example.newspeed.entity.PostLike;
import com.example.newspeed.entity.User;
import com.example.newspeed.enums.UserRole;
import com.example.newspeed.exception.exceptions.NotFoundException;
import com.example.newspeed.repository.LikeRepository;
import com.example.newspeed.repository.PostRepository;
import com.example.newspeed.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository usersRepository;

    // 게시글 전체
    @Transactional
    public Page<FindPostResponseDto> findPost(Pageable pageable){
        return postRepository.findAll(pageable).map(FindPostResponseDto::findPostDto);
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
        User user = usersRepository.findById(userId).orElseThrow(()->new NotFoundException("없음"));
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
    public FindPostResponseDto updatePost(Long postId, UpdatePostRequestDto updateDto) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("없음"));
        findPost.updatePost(updateDto);
        return new FindPostResponseDto(findPost);
    }

    @Transactional
    public ToggleLikeResponseDto toggleLike(Long postId, AuthUserDto authUserDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not Found Post"));
        User user = usersRepository.findById(authUserDto.getId()).orElseThrow(() -> new NotFoundException("Not Found User"));


        PostLike postLike = new PostLike(user, post);
        return null;
    }
}
