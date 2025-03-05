package com.example.authentication.Model.DTO.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String token;
    private String refresh_token;
}
