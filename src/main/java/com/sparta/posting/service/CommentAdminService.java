package com.sparta.posting.service;

import com.sparta.posting.dto.*;
import com.sparta.posting.entity.Comment;
import com.sparta.posting.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentAdminService {
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseAdminDto update(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("댓글이 존재하지 않습니다.")
        );
        comment.update(commentRequestDto);
        return new CommentResponseAdminDto(comment);
    }

    @Transactional
    public HttpResponseAdminDto delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("댓글이 존재하지 않습니다.")
        );
        commentRepository.delete(comment);
        return new HttpResponseAdminDto("댓글삭제가 완료되었습니다.", HttpStatus.UNAUTHORIZED.value());
    }
}
