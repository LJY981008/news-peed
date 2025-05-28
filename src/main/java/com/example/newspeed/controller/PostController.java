package com.example.newspeed.controller;


import com.example.newspeed.dto.Post.FindPostResponseDto;
import com.example.newspeed.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/post/{id}")
    public ResponseEntity<FindPostResponseDto> findByIdPost(@PathVariable Long id) {
        FindPostResponseDto findDto = postService.findById(id);
        return new ResponseEntity<>(findDto, HttpStatus.OK);

    }
}
