package com.example.newspeed.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news-peed")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
}
