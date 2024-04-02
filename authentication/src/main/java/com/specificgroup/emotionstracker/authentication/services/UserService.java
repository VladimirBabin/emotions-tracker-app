package com.specificgroup.emotionstracker.authentication.services;

import com.specificgroup.emotionstracker.authentication.dto.UserDto;
import com.specificgroup.emotionstracker.authentication.dto.CredentialsDto;
import com.specificgroup.emotionstracker.authentication.dto.SignUpDto;

public interface UserService {

    UserDto findByLogin(String login);

    UserDto login(CredentialsDto credentialsDto);

    UserDto register(SignUpDto signUpDto);
}
