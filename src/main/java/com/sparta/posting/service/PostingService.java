package com.sparta.posting.service;

import com.sparta.posting.dto.PostingRequestDto;
import com.sparta.posting.dto.ResponseDto;
import com.sparta.posting.entity.*;
import com.sparta.posting.jwt.JwtUtil;
import com.sparta.posting.repository.CommentRepository;
import com.sparta.posting.repository.PostingRepository;
import com.sparta.posting.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service                   // 서비스 레이어, 내부에서 자바 로직을 처리함(해당 클래스를 루트 컨테이너에 빈(Bean) 객체로 생성해주는 어노테이션입니다.)
@RequiredArgsConstructor   //초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다.
public class PostingService {
    private final PostingRepository postingRepository;   //@RequiredArgsConstructor 때문에 초기화 하지 않고도 사용가능
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    @Transactional             //컨트롤러와 결합해주는 역할을 한다.
    public Post createPosting(PostingRequestDto postingRequestDto, HttpServletRequest request) throws ResponseDto {
        String token = jwtUtil.resolveToken(request);
        Claims claims = null;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInformToken(token);
            } else {
                throw new ResponseDto("토큰이 유효하지 않습니다", HttpStatus.UNAUTHORIZED.value());
            }
        }
        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new ResponseDto("회원을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED.value())
        );
        Post post = new Post(postingRequestDto,user);
        postingRepository.save(post);
        return post;
    }

    @Transactional(readOnly = true)      //읽기전용으로 하면 약간의 성능적인 이점을 얻을 수 있다.
    public List<Post> getPostings() {
        List<Post> posts = postingRepository.findAllByOrderByCreatedAtDesc();
        for (Post post : posts) {
            post.comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(post.getId());
        }
        return posts;
    }

    @Transactional(readOnly = true)
    public Post getPostingById(Long id) throws ResponseDto {
        Post post = postingRepository.findById(id).orElseThrow(
                () -> new ResponseDto("게시물이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED.value())
        );
        post.comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(post.getId());
        return post;
    }

    @Transactional
    public Post update(Long id, PostingRequestDto postingRequestDto, HttpServletRequest request) throws ResponseDto {
        Post post = postingRepository.findById(id).orElseThrow(
                () -> new ResponseDto("게시물이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED.value())
        );
        String token = jwtUtil.resolveToken(request);
        Claims claims = null;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInformToken(token);
            } else {
                throw new ResponseDto("토큰이 유효하지 않습니다", HttpStatus.UNAUTHORIZED.value());
            }
        }
        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new ResponseDto("회원을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED.value())
        );
        if(user.getRole() == UserRoleEnum.ADMIN) {
            post.update(postingRequestDto);
            return post;
        }
        post = postingRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new ResponseDto("작성자만 수정할 수 있습니다.",HttpStatus.UNAUTHORIZED.value())
        );
        post.update(postingRequestDto);
        return post;
    }

    @Transactional
    public ResponseDto deletePosting(Long id, HttpServletRequest request) throws ResponseDto {
        Post post = postingRepository.findById(id).orElseThrow(
                () -> new ResponseDto("게시물이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED.value())
        );
        ResponseDto response = new ResponseDto("게시물삭제가 완료되었습니다.",HttpStatus.UNAUTHORIZED.value());
        String token = jwtUtil.resolveToken(request);
        Claims claims = null;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInformToken(token);
            } else {
                throw new ResponseDto("토큰이 유효하지 않습니다", HttpStatus.UNAUTHORIZED.value());
            }
        }
        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new ResponseDto("회원을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED.value())
        );
        if(user.getRole() == UserRoleEnum.ADMIN) {
            postingRepository.delete(post);
            return response;
        }
        post = postingRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new ResponseDto("작성자만 삭제할 수 있습니다.",HttpStatus.UNAUTHORIZED.value())
        );
        postingRepository.deleteById(id);
        return response;
    }
}
