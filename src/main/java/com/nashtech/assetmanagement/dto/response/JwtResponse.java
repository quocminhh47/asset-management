package com.nashtech.assetmanagement.dto.response;

import com.nashtech.assetmanagement.enums.UserState;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private UserState state;
    private List<String> roles;

    public JwtResponse(String accessToken, String id, String username,UserState state,List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.state = state;
        this.roles = roles;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }
}