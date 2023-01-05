package com.sparta.posting.service;


import com.sparta.posting.dto.LoginRequestDto;
import com.sparta.posting.dto.ResponseDto;
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

    private final JwtUtil jwtUtil;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseDto signup(SignupRequestDto signupRequestDto) throws ResponseDto {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return new ResponseDto("중복된 사용자가 존재합니다.",HttpStatus.UNAUTHORIZED.value());
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                return new ResponseDto("관리자 암호가 틀려 등록이 불가능합니다.",HttpStatus.UNAUTHORIZED.value());
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username,password,role);
        userRepository.save(user);

        ResponseDto response2 = new ResponseDto("회원가입 성공",200);
        return response2;
    }

    @Transactional(readOnly = true)
    public ResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) throws ResponseDto {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseDto("회원을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED.value())
        );

        if(!user.getPassword().equals(password)) {
            return new ResponseDto("회원을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED.value());
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(),user.getRole()));

        ResponseDto response1 = new ResponseDto("로그인성공",HttpStatus.UNAUTHORIZED.value());
        return response1;
    }
}
