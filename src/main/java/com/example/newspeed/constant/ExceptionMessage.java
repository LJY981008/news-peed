package com.example.newspeed.constant;

/**
 * 예외 메시지 상수 클래스
 *
 * @author 이준영
 */
public final class ExceptionMessage {
    private ExceptionMessage() {
        throw new IllegalStateException("상수 클래스는 인스턴스화할 수 없습니다.");
    }

    // Post 관련 예외 메시지
    public static final String POST_NOT_FOUND = "찾을 수 없는 게시물 입니다.";
    public static final String PAGE_NOT_FOUND = "해당 페이지에 게시글이 존재하지 않습니다.";
    public static final String DATE_POST_NOT_FOUND = "%s에 작성된 게시글이 존재하지 않습니다.";
    public static final String POST_UPDATE_NOT_FOUND = "수정할 게시글을 찾지 못했습니다.";
    public static final String SAME_CONTENT = "이전 내용과 동일한 내용입니다.";

    // User 관련 예외 메시지
    public static final String USER_NOT_FOUND = "로그인이 필요한 서비스입니다.";
    public static final String USER_NOT_FOUND_BY_ID = "존재하지 않는 사용자입니다.";

    // 권한 관련 예외 메시지
    public static final String UNAUTHORIZED = "권한이 없습니다";
    public static final String ADMIN_AUTHORITY = "관리자 권한";
    public static final String NORMAL_AUTHORITY = "정상적으로 권한이 확인되었습니다";

    // 좋아요 관련 예외 메시지
    public static final String LIKE_NOT_FOUND = "좋아요 정보를 찾을 수 없습니다.";

    // 댓글 관련 예외 메시지
    public static final String COMMENT_NOT_FOUND = "찾을 수 없는 댓글 입니다.";
    public static final String COMMENT_DELETED = "이미 삭제된 댓글 입니다.";
    public static final String COMMENT_UNAUTHORIZED = "댓글에 대한 권한이 없습니다.";
} 