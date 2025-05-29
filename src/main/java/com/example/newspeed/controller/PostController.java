package com.example.newspeed.controller;


import com.example.newspeed.dto.AuthUserDto;
import com.example.newspeed.dto.Post.FindPostResponseDto;
import com.example.newspeed.dto.post.CreatePostRequestDto;
import com.example.newspeed.dto.post.CreatePostResponseDto;
import com.example.newspeed.dto.post.DeletePostResponseDto;
import com.example.newspeed.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news-peed/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /* 토큰 설명
        private final JwtUtil jwtUtil;
        토큰 생성 : jwtUtil.createToken(1L, "LJY", UserRole.USER);
        토큰 생성 메서드 프로퍼티 타입 : public String createToken(Long userId, String name, UserRole userRole)
        토큰 생성 후 반환 :
        return ResponseEntity.status(HttpStatus.OK)
                    .header("Authorization", token)
                    .body(responseDto);

        인가된 객체 호출
        Optional<Authentication> authentication = Optional.ofNullable(
                SecurityContextHolder.getContext().getAuthentication()
        );
        if(authentication.isPresent() && authentication.get().getPrincipal() instanceof AuthUserDto){
            AuthUserDto authUserDto = (AuthUserDto) authentication.get().getPrincipal();
            Long userId = authUserDto.getId();
            String email = authUserDto.getEmail();
        }
     */


    //게시글 전체조회
    @GetMapping("/find-all")
    public ResponseEntity<List<FindPostResponseDto>> findPost() {
        List<FindPostResponseDto> findPostResponseDtoList = postService.findPost();
        return new ResponseEntity<>(findPostResponseDtoList, HttpStatus.OK);
    }

    // 게시글 단건 조회
    @GetMapping
    public ResponseEntity<FindPostResponseDto> findByIdPost(@RequestParam Long userId) {
        FindPostResponseDto findDto = postService.findById(userId);
        return new ResponseEntity<>(findDto, HttpStatus.OK);

    }
    // 게시글 생성
    @PostMapping("/create-posts")
    public ResponseEntity<CreatePostResponseDto> createPost(
            @RequestBody CreatePostRequestDto dto,
            @AuthenticationPrincipal AuthUserDto userDto) {
        CreatePostResponseDto post = postService.createPost(dto.getTitle(), dto.getContent(), dto.getImageUrl(),userDto.getId());

        return ResponseEntity.status(201).body(post);
    }
    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<DeletePostResponseDto> deletePost(@PathVariable Long postId) {
        DeletePostResponseDto deletePost = postService.deletePost(postId);

        return ResponseEntity.status(200).body(deletePost);
    }
}
