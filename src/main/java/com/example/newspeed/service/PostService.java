package com.example.newspeed.service;


import com.example.newspeed.dto.Post.FindPostResponseDto;
import com.example.newspeed.repository.CommentRepository;
import com.example.newspeed.repository.PostRepository;
import com.example.newspeed.repository.UsersRepository;
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
}
