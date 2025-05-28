package com.example.newspeed.service;


import com.example.newspeed.dto.Post.FindPostResponseDto;
import com.example.newspeed.dto.post.CreatePostResponseDto;
import com.example.newspeed.entity.Post;
import com.example.newspeed.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 조회
    public List<FindPostResponseDto> findPost(){
        return postRepository.findAll().stream().map(FindPostResponseDto::findPostDto).toList();
    }
    // 게시글 생성
    public CreatePostResponseDto createPost(String title, String content, String imageUrl) {
        Post post = new Post(title, content, imageUrl);
        postRepository.save(post);
        return new CreatePostResponseDto();
    }
}
