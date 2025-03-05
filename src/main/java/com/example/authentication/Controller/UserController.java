package com.example.authentication.Controller;

import com.example.authentication.Model.DTO.request.UserRequest;
import com.example.authentication.Model.DTO.response.ApiResponse;
import com.example.authentication.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public ApiResponse<?> getAll(){
        return userService.findAll();
    }

    @GetMapping("/info")
    public ApiResponse<?> getInfo(){
        return userService.getInfo();
    }

    @PostMapping("")
    public ApiResponse<?> create(@RequestBody @Valid UserRequest userRequest){
        return userService.create(userRequest);
    }

    @PutMapping("")
    public ApiResponse<?> update(@RequestBody @Valid UserRequest userRequest){
        return userService.update(userRequest);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id){
        return userService.delete(id);
    }
}
