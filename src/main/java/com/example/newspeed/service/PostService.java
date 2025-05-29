package com.example.newspeed.service;


import com.example.newspeed.dto.Post.FindPostResponseDto;
import com.example.newspeed.dto.Post.UpdatePostRequestDto;
import com.example.newspeed.entity.Post;
import com.example.newspeed.exception.exceptions.NotFoundException;
import com.example.newspeed.repository.PostRepository;
import com.example.newspeed.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;

    // 게시글 전체
    public List<FindPostResponseDto> findPost(){
        return postRepository.findAll().stream().map(FindPostResponseDto::findPostDto).toList();
    }

    // 게시글 단건 조회
    public FindPostResponseDto findById(Long userId) {
        Post findPost = postRepository.findById(userId).orElseThrow(() -> new NotFoundException("없음"));
        return new FindPostResponseDto(findPost);
    }
    // 게시글 생성
    public com.example.newspeed.dto.post.CreatePostResponseDto createPost(String title, String content, String imageUrl) {
        Post post = new Post(title, content, imageUrl);
        postRepository.save(post);
        return new com.example.newspeed.dto.post.CreatePostResponseDto();
    }

    // 게시글 삭제
    public com.example.newspeed.dto.post.DeletePostResponseDto deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 게시글이 없습니다."));
//        jwt토큰이 일치하지 않는다면 Autheorization처리
        postRepository.delete(post);

        return new com.example.newspeed.dto.post.DeletePostResponseDto();
    }

    // 게시글 수정
    public FindPostResponseDto updatePost(Long postId, UpdatePostRequestDto updateDto) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("없음"));
        findPost.updatePost(updateDto);
        return new FindPostResponseDto(findPost);
    }
}
