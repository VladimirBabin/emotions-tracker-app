package com.specificgroup.emotionstracker.authorization.mappers;

import com.specificgroup.emotionstracker.authorization.domain.User;
import com.specificgroup.emotionstracker.authorization.dto.SignUpDto;
import com.specificgroup.emotionstracker.authorization.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}

