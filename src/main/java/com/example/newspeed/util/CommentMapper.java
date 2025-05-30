package com.example.newspeed.util;

import com.example.newspeed.dto.comment.*;
import com.example.newspeed.entity.Comment;

/**
 * <p>댓글 관련 응답 Dto Mapper</p>
 *
 * @author 이준영
 */
public class CommentMapper {
    private CommentMapper() {
    }

    public static <T extends BaseCommentResponseDto> T toDto(Comment comment, Class<T> dtoClass) {
        try{
            return dtoClass.getConstructor(Comment.class).newInstance(comment);
        } catch (Exception e){
            throw new RuntimeException("Comment -> Dto 변환 실패");
        }
    }
}
