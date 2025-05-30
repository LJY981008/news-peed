package com.example.newspeed.constant;

/**
 * <p>상수 리스트</p>
 */
public class Const {

    public static final String BASE_URL = "/news-peed";

    public static final String COMMENT_URL = BASE_URL + "/comments";
    public static final String FOLLOWS_URL = BASE_URL + "/follows";
    public static final String POST_URL = BASE_URL + "/post";
    public static final String USER_URL = BASE_URL + "/users";

    public static final String TOKEN_KEY = "mysecretkeyverylongandsecureforjwttokengenerationwhichisatleast32bytes";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final long TOKEN_TIME = 60 * 60 * 1000L;

    private Const() {
    }
}
