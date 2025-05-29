package com.example.newspeed.controller;


import com.example.newspeed.dto.Post.FindPostResponseDto;
import com.example.newspeed.dto.Post.UpdatePostRequestDto;
import com.example.newspeed.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news-peed/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    //게시글 전체조회
    @GetMapping("/find-all")
    public ResponseEntity<List<FindPostResponseDto>> findPost() {
        List<FindPostResponseDto> findPostResponseDtoList = postService.findPost();
        return new ResponseEntity<>(findPostResponseDtoList, HttpStatus.OK);
    }

    // 게시글 단건 조회
    @GetMapping
    public ResponseEntity<FindPostResponseDto> findByIdPost(@RequestParam Long postId) {
        FindPostResponseDto findDto = postService.findById(postId);
        return new ResponseEntity<>(findDto, HttpStatus.OK);

    }

    // 게시글 수정
    @PatchMapping
    public ResponseEntity<FindPostResponseDto> updatePost(@RequestParam Long userId, @RequestBody UpdatePostRequestDto updateDto) {
       FindPostResponseDto findPostResponseDto = postService.updatePost(userId, updateDto.getTitle(), updateDto.getContents(), updateDto.getPassword());
       return new ResponseEntity<>(findPostResponseDto, HttpStatus.OK);
    }
}
