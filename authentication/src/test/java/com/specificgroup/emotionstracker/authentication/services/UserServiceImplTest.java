package com.specificgroup.emotionstracker.authentication.services;

import com.specificgroup.emotionstracker.authentication.domain.User;
import com.specificgroup.emotionstracker.authentication.dto.UserDto;
import com.specificgroup.emotionstracker.authentication.exceptions.AppException;
import com.specificgroup.emotionstracker.authentication.mappers.UserMapper;
import com.specificgroup.emotionstracker.authentication.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper, passwordEncoder);
    }

    @Test
    void whenFoundByLoginUserDtoReturned() {
        // given
        String login = "john-doe";
        UUID id = UUID.randomUUID();
        User user = new User(id, "john", "doe", login, null);
        UserDto userDto = new UserDto(id, "john", "doe", login, "token");
        given(userRepository.findByLogin(login))
                .willReturn(Optional.of(user));
        given(userMapper.toUserDto(user))
                .willReturn(userDto);

        // when
        UserDto byLogin = userService.findByLogin(login);

        // then
        verify(userRepository).findByLogin(login);
        verify(userMapper).toUserDto(user);
        then(byLogin).isEqualTo(userDto);
    }

    @Test
    void whenUserNotFoundThenAppExceptionWithNotFoundStatusThrown() {
        // given
        String login = "john-doe";
        given(userRepository.findByLogin(login))
                .willReturn(Optional.empty());

        // when
        thenThrownBy(() -> userService.findByLogin(login)).isInstanceOf(AppException.class);
        thenThrownBy(() -> userService.findByLogin(login)).hasMessage("Unknown user");
    }


}