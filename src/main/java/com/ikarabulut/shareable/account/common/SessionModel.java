package com.ikarabulut.shareable.account.common;

import java.util.Date;
import java.util.UUID;

public class SessionModel {
    private String Jwt;
    private String createdAt;
    private UUID userId;

    public String getJwt() {
        return Jwt;
    }

    public void setJwt(String jwt) {
        Jwt = jwt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
