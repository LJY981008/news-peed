//package com.example.newspeed.dto.comment;
//
//import com.example.newspeed.entity.Comment;
//import lombok.Builder;
//import lombok.Getter;
//
//@Builder
//@Getter
//public class CommentFindResponseDto {
//
//    private Long id;
//    private String content;
//    private String writer;
//    private int likeCount;
//    private String createdAt;
//    private String modifiedAt;
//
//    //TODO 작성자 이름 좋아요 카운트 적용 필요
//    public static CommentFindResponseDto from(Comment comment) {
//        return CommentFindResponseDto.builder()
//                .id(comment.getId())
//                .content(comment.getContent())
//                //.writer(comment.getWriter().getName())
//                //.likeCount(comment.getLikeCount())
//                .createdAt(comment.getCreatedAt().toString())
//                .modifiedAt(comment.getModifiedAt().toString())
//                .build();
//    }
//}
