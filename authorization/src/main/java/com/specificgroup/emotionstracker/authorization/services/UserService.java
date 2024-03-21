package com.specificgroup.emotionstracker.authorization.services;

import com.specificgroup.emotionstracker.authorization.dto.CredentialsDto;
import com.specificgroup.emotionstracker.authorization.dto.SignUpDto;
import com.specificgroup.emotionstracker.authorization.dto.UserDto;

public interface UserService {

    UserDto findByLogin(String login);

    UserDto login(CredentialsDto credentialsDto);

    UserDto register(SignUpDto signUpDto);
}
