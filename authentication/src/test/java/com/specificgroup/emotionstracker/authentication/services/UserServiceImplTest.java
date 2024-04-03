package com.specificgroup.emotionstracker.authentication.services;

import com.specificgroup.emotionstracker.authentication.domain.User;
import com.specificgroup.emotionstracker.authentication.dto.CredentialsDto;
import com.specificgroup.emotionstracker.authentication.dto.SignUpDto;
import com.specificgroup.emotionstracker.authentication.dto.UserDto;
import com.specificgroup.emotionstracker.authentication.exceptions.AppException;
import com.specificgroup.emotionstracker.authentication.mappers.UserMapper;
import com.specificgroup.emotionstracker.authentication.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.CharBuffer;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserService userService;

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
    void whenUserNotFoundByLogInThenAppExceptionWithNotFoundStatusThrown() {
        // given
        String login = "john-doe";
        given(userRepository.findByLogin(login))
                .willReturn(Optional.empty());

        // then
        thenExceptionOfType(AppException.class)
                .isThrownBy(() -> userService.findByLogin(login)).withMessage("Unknown user");
    }

    @Test
    void whenUserNotFoundOnLogInThenAppExceptionWithNotFoundStatusThrown() {
        // given
        String login = "john-doe";
        CredentialsDto dto = new CredentialsDto(login, null);
        given(userRepository.findByLogin(dto.getLogin()))
                .willReturn(Optional.empty());

        // then
        thenExceptionOfType(AppException.class)
                .isThrownBy(() -> userService.login(dto)).withMessage("Unknown user");
    }

    @Test
    void whenPasswordMatchesThenUserDtoIsReturned() {
        // given
        String login = "john-doe";
        char[] password = {'s', 'e', 'c', 'r', 'e', 't'};
        CredentialsDto credentialsDto = new CredentialsDto(login, password);
        UUID id = UUID.randomUUID();
        User user = new User(id, "john", "doe", login, "secret");
        given(userRepository.findByLogin(credentialsDto.getLogin()))
                .willReturn(Optional.of(user));
        UserDto expected = new UserDto(id, "john", "doe", login, "token");
        given(userMapper.toUserDto(user))
                .willReturn(expected);
        given(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword()))
                .willReturn(true);

        // when
        UserDto userDto = userService.login(credentialsDto);

        // then
        verify(userRepository).findByLogin(credentialsDto.getLogin());
        then(userDto).isEqualTo(expected);
    }

    @Test
    void whenPasswordsDontMatchThenAppExceptionWithInvalidPasswordMessageIsThrown() {
        // given
        String login = "john-doe";
        char[] password = {'s', 'e', 'c', 'r', 'e', 't'};
        CredentialsDto credentialsDto = new CredentialsDto(login, password);
        UUID id = UUID.randomUUID();
        User user = new User(id, "john", "doe", login, "super-secret");
        given(userRepository.findByLogin(credentialsDto.getLogin()))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword()))
                .willReturn(false);

        // then
        thenExceptionOfType(AppException.class)
                .isThrownBy(() -> userService.login(credentialsDto))
                .withMessage("Invalid password");
    }

    @Test
    void whenLoginAlreadyUsedThenAppExceptionOnRegisterIsThrown() {
        // given
        String login = "john-doe";
        SignUpDto signUpDto = new SignUpDto("john", "doe", login, "secret".toCharArray());
        User user = new User(UUID.randomUUID(), "john", "doe", login, "secret");
        given(userRepository.findByLogin(signUpDto.getLogin()))
                .willReturn(Optional.of(user));

        // then
        thenExceptionOfType(AppException.class)
                .isThrownBy(() -> userService.register(signUpDto))
                .withMessage("Login already exists");
    }

    @Test
    void whenRegisteredSuccessfullyThenUserCreatedAndUserDtoReturned() {
        String login = "john-doe";
        UUID id = UUID.randomUUID();
        SignUpDto signUpDto = new SignUpDto("john", "doe", login, "secret".toCharArray());
        User mockUser = BDDMockito.mock();
        UserDto expected = new UserDto(id, "john", "doe", login, "token");
        given(userRepository.findByLogin(signUpDto.getLogin()))
                .willReturn(Optional.empty());
        given(userMapper.signUpToUser(signUpDto))
                .willReturn(mockUser);
        given(passwordEncoder.encode(CharBuffer.wrap(signUpDto.getPassword())))
                .willReturn("secret");
        given(userRepository.save(mockUser))
                .will(returnsFirstArg());
        given(userMapper.toUserDto(mockUser))
                .willReturn(expected);

        // when
        UserDto registeredDto = userService.register(signUpDto);

        // then
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.captor();
        verify(userRepository).findByLogin(signUpDto.getLogin());
        verify(passwordEncoder).encode(CharBuffer.wrap("secret"));
        verify(mockUser).setPassword(passwordCaptor.capture());
        then(passwordCaptor.getValue()).isEqualTo("secret");
        verify(userRepository).save(mockUser);
        then(registeredDto).isEqualTo(expected);
    }
}