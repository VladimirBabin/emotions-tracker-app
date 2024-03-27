package com.specificgroup.emotionstracker.authorization.controllers;

import com.specificgroup.emotionstracker.authorization.configuration.UserAuthProvider;
import com.specificgroup.emotionstracker.authorization.dto.CredentialsDto;
import com.specificgroup.emotionstracker.authorization.dto.SignUpDto;
import com.specificgroup.emotionstracker.authorization.dto.UserDto;
import com.specificgroup.emotionstracker.authorization.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);

        userDto.setToken(userAuthProvider.createToken(userDto.getLogin(), userDto.getId().toString()));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    private ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
        UserDto userDto = userService.register(signUpDto);
        userDto.setToken(userAuthProvider.createToken(userDto.getLogin(), userDto.getId().toString()));
        return ResponseEntity.created(URI.create("/users/" + userDto.getId()))
                .body(userDto);
    }
}
