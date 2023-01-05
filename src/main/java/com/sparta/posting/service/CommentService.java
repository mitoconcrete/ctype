package com.sparta.posting.service;

import com.sparta.posting.dto.CommentRequestDto;
import com.sparta.posting.entity.*;
import com.sparta.posting.jwt.JwtUtil;
import com.sparta.posting.repository.CommentRepository;
import com.sparta.posting.repository.PostRepository;
import com.sparta.posting.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Comment addComment(Long postingId, CommentRequestDto commentRequestDto, HttpServletRequest request) throws ResponseDto {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {


            if (jwtUtil.validateToken(token)) {

                claims = jwtUtil.getUserInformToken(token);
            } else {
                throw new ResponseDto("토큰이 유효하지 않습니다", HttpStatus.UNAUTHORIZED.value());
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new ResponseDto("회원을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED.value())
            );
            Post post = postRepository.findById(postingId).orElseThrow(
                    () -> new ResponseDto("게시물이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED.value())
            );
            Comment comment = new Comment(commentRequestDto, user, postingId);
            post.addcomment(comment);
            commentRepository.save(comment);

            return comment;
        } else {
            return null;
        }
    }

    @Transactional
    public Comment update(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request) throws ResponseDto {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {

            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInformToken(token);
            } else {
                throw new ResponseDto("토큰이 유효하지 않습니다", HttpStatus.UNAUTHORIZED.value());
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new ResponseDto("회원을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED.value())
            );
            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new ResponseDto("댓글이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED.value())
            );
            Post post = postRepository.findById(comment.getPostId()).orElseThrow(
                    () -> new NullPointerException("게시물이 존재하지 않습니다.")
            );
            post.removecomment(comment);
            if(user.getRole() == UserRoleEnum.ADMIN) {
                comment.update(commentRequestDto);
                post.addcomment(comment);
                return comment;
            }
            if (user.getId().equals(comment.getUserId())) {
                comment.update(commentRequestDto);
                post.addcomment(comment);
                return comment;
            } else {
                throw new ResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.UNAUTHORIZED.value());
            }
        } else {
            return null;
        }
    }

    @Transactional
    public ResponseDto delete(Long id, HttpServletRequest request) throws ResponseDto {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {

            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInformToken(token);
            } else {
                throw new ResponseDto("토큰이 유효하지 않습니다", HttpStatus.UNAUTHORIZED.value());
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new ResponseDto("회원을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED.value())
            );
            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new ResponseDto("댓글이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED.value())
            );
            Post post = postRepository.findById(comment.getPostId()).orElseThrow(
                    () -> new NullPointerException("게시물이 존재하지 않습니다.")
            );
            ResponseDto response = new ResponseDto("댓글삭제가 완료되었습니다.", HttpStatus.UNAUTHORIZED.value());
            if(user.getRole() == UserRoleEnum.ADMIN) {
                post.removecomment(comment);
                commentRepository.delete(comment);
                return response;
            }
            if (user.getId().equals(comment.getUserId())) {
                post.removecomment(comment);
                commentRepository.delete(comment);
                return response;
            } else {
                new ResponseDto("작성자만 삭제할 수 있습니다.", HttpStatus.UNAUTHORIZED.value());
            }
        } else {
            throw new ResponseDto("토큰이 유효하지 않습니다", HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }

}
