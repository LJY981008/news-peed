package com.example.newspeed.controller;


import com.example.newspeed.dto.Post.FindPostResponseDto;
import com.example.newspeed.dto.post.CreatePostRequestDto;
import com.example.newspeed.dto.post.CreatePostResponseDto;
import com.example.newspeed.dto.post.DeletePostResponseDto;
import com.example.newspeed.service.PostService;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news-peed")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    // 게시글 조회
    @GetMapping
    public ResponseEntity<List<FindPostResponseDto>> findPost() {
        List<FindPostResponseDto> findPostResponseDtoList = postService.findPost();
        return new ResponseEntity<>(findPostResponseDtoList, HttpStatus.OK);
    }
    //게시글 생성
    @PostMapping("/create-posts")
    public ResponseEntity<CreatePostResponseDto> createPost(@RequestBody CreatePostRequestDto dto) {
        CreatePostResponseDto post = postService.createPost(dto.getTitle(), dto.getContent(), dto.getImageUrl());

        return ResponseEntity.status(201).body(post);
    }
    // 게시글 삭제
    @DeleteMapping("/delete-posts/{postId}")
    public ResponseEntity<DeletePostResponseDto> deletePost(@PathVariable Long postId) {
        DeletePostResponseDto deletePostResponseDto = postService.deletePost(postId);
        return ResponseEntity.status(200).body(deletePostResponseDto);
    }
}
