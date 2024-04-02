package com.specificgroup.emotionstracker.authentication.mappers;

import com.specificgroup.emotionstracker.authentication.domain.User;
import com.specificgroup.emotionstracker.authentication.dto.UserDto;
import com.specificgroup.emotionstracker.authentication.dto.SignUpDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "token", ignore = true)
    UserDto toUserDto(User user);
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}

