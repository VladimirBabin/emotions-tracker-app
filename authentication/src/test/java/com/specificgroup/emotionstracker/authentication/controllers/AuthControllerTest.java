package com.specificgroup.emotionstracker.authentication.controllers;

import com.specificgroup.emotionstracker.authentication.configuration.UserAuthProvider;
import com.specificgroup.emotionstracker.authentication.dto.CredentialsDto;
import com.specificgroup.emotionstracker.authentication.dto.ErrorDto;
import com.specificgroup.emotionstracker.authentication.dto.SignUpDto;
import com.specificgroup.emotionstracker.authentication.dto.UserDto;
import com.specificgroup.emotionstracker.authentication.exceptions.AppException;
import com.specificgroup.emotionstracker.authentication.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserAuthProvider userAuthProvider;
    @Autowired
    private JacksonTester<UserDto> userDtoJacksonTester;
    @Autowired
    private JacksonTester<ErrorDto> errorJacksonTester;
    @Autowired
    private JacksonTester<CredentialsDto> credentialsDtoJacksonTester;
    @Autowired
    private JacksonTester<SignUpDto> signUpDtoJacksonTester;

    @Test
    void whenLogInSuccessfulThenTokenCreatedAndUserDtoReturned() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        String login = "john-doe";
        String token = "token";
        CredentialsDto credentials = new CredentialsDto(login, "secret".toCharArray());
        UserDto userDto = new UserDto(id, "john", "doe", login, null);
        given(userService.login(credentials))
                .willReturn(userDto);
        given(userAuthProvider.createToken(login, id.toString()))
                .willReturn(token);

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                                .content(credentialsDtoJacksonTester.write(credentials).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(userDtoJacksonTester.write(userDto).getJson());
    }

    @Test
    void whenUserNotFoundOnLoginThenResponseNotFoundWithErrorDto() throws Exception {
        // given
        String login = "john-doe";
        CredentialsDto credentials = new CredentialsDto(login, "secret".toCharArray());
        ErrorDto errorDto = new ErrorDto("Unknown user");
        given(userService.login(credentials))
                .willThrow(new AppException("Unknown user", HttpStatus.NOT_FOUND));

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                                .content(credentialsDtoJacksonTester.write(credentials).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        then(response.getContentAsString()).isEqualTo(errorJacksonTester.write(errorDto).getJson());
    }

    @Test
    void whenUserCredentialsDontMatchOnLoginThenBadRequestResponseWithErrorDto() throws Exception {
        // given
        String login = "john-doe";
        CredentialsDto credentials = new CredentialsDto(login, "secret".toCharArray());
        ErrorDto errorDto = new ErrorDto("Invalid password");
        given(userService.login(credentials))
                .willThrow(new AppException("Invalid password", HttpStatus.BAD_REQUEST));

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                                .content(credentialsDtoJacksonTester.write(credentials).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        then(response.getContentAsString()).isEqualTo(errorJacksonTester.write(errorDto).getJson());
    }

    @Test
    void whenRegisterSuccessfulThenTokenCreatedAndUserDtoReturned() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        String login = "john-doe";
        String token = "token";
        SignUpDto signUpDto = new SignUpDto("john", "doe", login, "secret".toCharArray());
        UserDto userDto = new UserDto(id, "john", "doe", login, null);
        given(userService.register(signUpDto))
                .willReturn(userDto);
        given(userAuthProvider.createToken(login, id.toString()))
                .willReturn(token);

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                                .content(signUpDtoJacksonTester.write(signUpDto).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        then(response.getContentAsString()).isEqualTo(userDtoJacksonTester.write(userDto).getJson());
    }

    @Test
    void whenLoginAlreadyRegisteredThenBadRequestReturnedWithErrorDto() throws Exception {
        // given
        String login = "john-doe";
        SignUpDto signUpDto = new SignUpDto("john", "doe", login, "secret".toCharArray());
        ErrorDto errorDto = new ErrorDto("Login already exists");
        given(userService.register(signUpDto))
                .willThrow(new AppException("Login already exists", HttpStatus.BAD_REQUEST));

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                                .content(signUpDtoJacksonTester.write(signUpDto).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        then(response.getContentAsString()).isEqualTo(errorJacksonTester.write(errorDto).getJson());
    }


}