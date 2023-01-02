package com.sparta.posting.service;

import com.sparta.posting.dto.CommentRequestDto;
import com.sparta.posting.dto.CommentResponseDto;
import com.sparta.posting.dto.HttpResponseDto;
import com.sparta.posting.entity.*;
import com.sparta.posting.repository.CommentRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    @Transactional
    public CommentResponseDto addComment(Long postingId, CommentRequestDto commentRequestDto, User user) {
            Comment comment = new Comment(commentRequestDto, user, postingId);
            commentRepository.save(comment);
            return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto update(Long commentid, CommentRequestDto commentRequestDto, User user) throws HttpResponseDto {
            Comment comment = commentRepository.findById(commentid).orElseThrow(
                    () -> new HttpResponseDto("댓글이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED.value())
            );
            if (user.getId().equals(comment.getUserId())) {
                comment.update(commentRequestDto);
                return new CommentResponseDto(comment);
            } else {
                throw new HttpResponseDto("작성자만 수정할 수 있습니다.", 400);
            }

    }
    @Transactional
    public HttpResponseDto delete(Long commentid, User user) throws HttpResponseDto {
            Comment comment = commentRepository.findById(commentid).orElseThrow(
                    () -> new HttpResponseDto("댓글이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED.value())
            );
            if (user.getId().equals(comment.getUserId())) {
                commentRepository.delete(comment);
                return new HttpResponseDto("댓글삭제가 완료되었습니다.", HttpStatus.UNAUTHORIZED.value());
            } else {
                return new HttpResponseDto("작성자만 삭제할 수 있습니다.",400);
            }
    }
}
