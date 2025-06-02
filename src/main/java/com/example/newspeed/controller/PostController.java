package com.example.newspeed.controller;


import com.example.newspeed.constant.Const;
import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.dto.post.FindPostResponseDto;
import com.example.newspeed.dto.post.CreatePostRequestDto;
import com.example.newspeed.dto.post.CreatePostResponseDto;
import com.example.newspeed.dto.post.DeletePostResponseDto;
import com.example.newspeed.dto.post.*;
import com.example.newspeed.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(Const.POST_URL)
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시글 조회
     *
     * @author 김태현
     * @param date ( 선택 ) 조회할 날짜, 지정하지 않으면 전체 게시글을 조회
     * @param pageable 페이징 및 정렬 정보 ( 기본값 size = 10, 정렬기준 modifiedAt, 내림차순
     * @return 페이징된 게시글 응답 객체 (Page<FindPostResponseDto>)
     */
    //게시글 전체조회
    // modifiedAt 기준으로 정렬
    @GetMapping("/find-all")
    public ResponseEntity<Page<FindPostResponseDto>> findPost(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(size = 10, sort = "modifiedAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<FindPostResponseDto> findPostResponseDtoList = (date == null) ? postService.findAllPost(pageable) : postService.findAllByDate(date, pageable);
        return new ResponseEntity<>(findPostResponseDtoList, HttpStatus.OK);
    }

    /**
     * @author 김태현
     * @param postId 조회활 게시글의 ID
     * @return 조회된 게시글 정보를 담은 응답 DTO (FindPostResponseDto)
     */
    // 게시글 단건 조회
    @GetMapping
    public ResponseEntity<FindPostResponseDto> findByIdPost(@RequestParam Long postId) {
        FindPostResponseDto findDto = postService.findById(postId);
        return new ResponseEntity<>(findDto, HttpStatus.OK);

    }

    /**
     * @author 김도연
     * @param userDto 인증된 사용자 정보 {@link AuthUserDto}
     * @param pageable 페이징 및 정렬 정보 ( 기본값 size = 10, 정렬기준 modifiedAt, 내림차순
     * @return 페이징된 게시글 응답 객체 (Page<FindPostResponseDto>)
     */
    @GetMapping("/find-follow")
    public ResponseEntity<Page<FindPostResponseDto>> findFollowingPost(
            @AuthenticationPrincipal AuthUserDto userDto,
            @PageableDefault(size = 10, sort = "modifiedAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Long currentUserId = userDto.getId();
        Page<FindPostResponseDto> findPostResponseDtoList = postService.findFollowingPosts(currentUserId, pageable);

        return new ResponseEntity<>(findPostResponseDtoList, HttpStatus.OK);
    }

    /**
     * <p>게시글 생성</p>
     *
     * @param dto 게시글 생성에 필요한 title, content, imageUrl {@link CreatePostRequestDto}
     * @param userDto 인증된 사용자 정보 {@link AuthUserDto}
     * @return 생성 성공시 {@code 201 CREATED} {@link CreatePostResponseDto} 포함하는 {@link ResponseEntity}
     * @author 윤희준
     */
    @PostMapping("/create-posts")
    public ResponseEntity<CreatePostResponseDto> createPost(
            @RequestBody @Valid CreatePostRequestDto dto,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        CreatePostResponseDto post = postService.createPost(dto.getTitle(), dto.getContent(), dto.getImageUrl(),userDto);

        return ResponseEntity.status(201).body(post);
    }

    /**
     * <p>게시글 삭제</p>
     *
     * @param postId 게시글의 pk값
     * @param authUserDto 인증된 사용자 정보 {@link AuthUserDto}
     * @return 삭제 성공시 {@code 200 OK}, {@link DeletePostResponseDto} 를 포함하는 {@link ResponseEntity}
     */
    @DeleteMapping("/delete-posts/{postId}")
    public ResponseEntity<DeletePostResponseDto> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        DeletePostResponseDto deletePost = postService.deletePost(postId, authUserDto);

        return ResponseEntity.status(200).body(deletePost);
    }

    /**
     * 게시글 수정
     *
     * @author 김태현
     * @param postId 수정할 게시글의 ID
     * @param updateDto 수정할 게시글의 제목, 내용이 담긴 요청 DTO
     * @param authUserDto 로그인한 사용자 정보 ( 작성자 검증에 사용 )
     * @return {@code 200 ok} 수정된 게시글 정보를 담은 응답 DTO
     */
    // 게시글 수정
    @PatchMapping("/post-update/{postId}")
    public ResponseEntity<FindPostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequestDto updateDto,
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        FindPostResponseDto findPostResponseDto = postService.updatePost(postId, authUserDto, updateDto);
        return new ResponseEntity<>(findPostResponseDto, HttpStatus.OK);
    }

    /**
     * <p>좋아요 기능</p>
     *
     * @param postId      좋아요가 눌린 게시글 Index
     * @param authUserDto 로그인된 사용자 정보
     * @return 상태코드와 {@link GetLikeResponseDto}
     * @author 이준영
     */
    @PatchMapping("/like")
    public ResponseEntity<GetLikeResponseDto> toggleLike(
            @RequestParam Long postId,
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        postService.toggleLike(postId, authUserDto);
        GetLikeResponseDto postLike = postService.getPostLike(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postLike);
    }
}
