package com.example.newspeed.controller;


import com.example.newspeed.constant.Const;
import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.dto.post.FindPostResponseDto;
import com.example.newspeed.dto.post.CreatePostRequestDto;
import com.example.newspeed.dto.post.CreatePostResponseDto;
import com.example.newspeed.dto.post.DeletePostResponseDto;
import com.example.newspeed.dto.post.*;
import com.example.newspeed.entity.Post;
import com.example.newspeed.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

//import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping(Const.POST_URL)
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
    // createdAt 기준으로 정렬
    @GetMapping("/find-all")
    public ResponseEntity<Page<FindPostResponseDto>> findPost(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<FindPostResponseDto> findPostResponseDtoList = postService.findPost(pageable);
        return new ResponseEntity<>(findPostResponseDtoList, HttpStatus.OK);
    }

    // 게시글 단건 조회
    @GetMapping
    public ResponseEntity<FindPostResponseDto> findByIdPost(@RequestParam Long postId) {
        FindPostResponseDto findDto = postService.findById(postId);
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
    public ResponseEntity<DeletePostResponseDto> deletePost(@PathVariable Long postId, @AuthenticationPrincipal AuthUserDto authUserDto) {
        DeletePostResponseDto deletePost = postService.deletePost(postId, authUserDto);

        return ResponseEntity.status(200).body(deletePost);
    }

    // 게시글 수정
    @PatchMapping("/post-update/{postId}")
    public ResponseEntity<FindPostResponseDto> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequestDto updateDto) {
       FindPostResponseDto findPostResponseDto = postService.updatePost(postId, updateDto);
       return new ResponseEntity<>(findPostResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/like")
    public ResponseEntity<ToggleLikeResponseDto> toggleLike(
        @RequestParam Long postId,
        @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        postService.toggleLike(postId, authUserDto);
        return ResponseEntity.status(HttpStatus.OK).body();
    }
}
