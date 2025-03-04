package com.example.authentication.Model.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
public class ErrorResponse{
    int status;
    String message;
}
