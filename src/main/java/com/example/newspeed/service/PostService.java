package com.example.newspeed.service;


import com.example.newspeed.dto.Post.FindPostResponseDto;
import com.example.newspeed.entity.Post;
import com.example.newspeed.exception.exceptions.NotFoundException;
import com.example.newspeed.repository.PostRepository;
import com.example.newspeed.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 전체
    public List<FindPostResponseDto> findPost(){
        return postRepository.findAll().stream().map(FindPostResponseDto::findPostDto).toList();
    }

    // 게시글 단건 조회
    public FindPostResponseDto findById(Long id) {
        Post findPost = postRepository.findById(id).orElseThrow(() -> new NotFoundException("없음"));
        return new FindPostResponseDto(findPost);
    }
}
