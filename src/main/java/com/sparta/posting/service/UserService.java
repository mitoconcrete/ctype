package com.sparta.posting.service;


import com.sparta.posting.dto.LoginRequestDto;
import com.sparta.posting.dto.HttpResponseDto;
import com.sparta.posting.dto.SignupRequestDto;
import com.sparta.posting.entity.User;
import com.sparta.posting.entity.UserRoleEnum;
import com.sparta.posting.jwt.JwtUtil;
import com.sparta.posting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostService postService;


    private final JwtUtil jwtUtil;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public HttpResponseDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return new HttpResponseDto("중복된 사용자가 존재합니다.",400);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                return new HttpResponseDto("관리자 암호가 틀려 등록이 불가능합니다.",HttpStatus.UNAUTHORIZED.value());
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username,password,role);
        userRepository.save(user);

        HttpResponseDto response2 = new HttpResponseDto("회원가입 성공",HttpStatus.UNAUTHORIZED.value());
        return response2;
    }

    @Transactional(readOnly = true)
    public HttpResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("회원을 찾을 수 없습니다.")
        );

        if(!user.getPassword().equals(password)) {
            return new HttpResponseDto("회원을 찾을 수 없습니다.", 400);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(),user.getRole()));

        HttpResponseDto response1 = new HttpResponseDto("로그인성공",HttpStatus.UNAUTHORIZED.value());
        return response1;
    }

    @Transactional
    public HttpResponseDto delte(User user) {
        postService.deletePostByUser(user);
        userRepository.delete(user);
        return new HttpResponseDto("회원탈퇴 성공!", HttpStatus.UNAUTHORIZED.value());
    }
}
