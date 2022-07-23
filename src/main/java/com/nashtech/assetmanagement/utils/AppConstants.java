package com.nashtech.assetmanagement.utils;

public class AppConstants {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "20";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";

    public static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v3 (OpenAPI)
            "/user/api/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/api-docs",
            "swagger-ui.html"
            // other public endpoints of your API may be appended to this array
    };

}
