package com.example.authentication.Model.Mapper;

import com.example.authentication.Model.DTO.request.UserRequest;
import com.example.authentication.Model.DTO.response.UserResponse;
import com.example.authentication.Model.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    User toUser(UserRequest userRequest);
}
