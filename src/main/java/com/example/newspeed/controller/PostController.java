package com.example.newspeed.controller;


import com.example.newspeed.dto.Post.FindPostResponseDto;
import com.example.newspeed.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news-peed")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    //게시글 조회
    @GetMapping
    public ResponseEntity<List<FindPostResponseDto>> findPost() {
        List<FindPostResponseDto> findPostResponseDtoList = postService.findPost();
        return new ResponseEntity<>(findPostResponseDtoList, HttpStatus.OK);
    }
}
