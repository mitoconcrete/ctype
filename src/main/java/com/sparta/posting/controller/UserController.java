package com.sparta.posting.controller;

import com.sparta.posting.dto.LoginRequestDto;
import com.sparta.posting.dto.HttpResponseDto;
import com.sparta.posting.dto.SignupRequestDto;
import com.sparta.posting.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @PostMapping("/signup")
    public HttpResponseDto signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {      //@Valid로 SignupRequestDto에 제한식을 걸어서 회원가입한다.
        return userService.signup(signupRequestDto);
    }

    @PostMapping("/login")
    public HttpResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return userService.login(loginRequestDto, response);             //로그인에 성공하면 response에 jwt를 붙여준다.
    }
}
