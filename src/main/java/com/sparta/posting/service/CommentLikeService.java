package com.sparta.posting.service;

import com.sparta.posting.dto.HttpResponseDto;
import com.sparta.posting.entity.Comment;
import com.sparta.posting.entity.CommentLike;
import com.sparta.posting.entity.User;
import com.sparta.posting.repository.CommentLikeRepository;
import com.sparta.posting.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public HttpResponseDto toggleLike(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("댓글이 존재하지 않습니다.")
        );
        if (commentLikeRepository.findByCommentIdAndUserId(commentId, user.getId()).isPresent()) {
            commentLikeRepository.deleteByCommentIdAndUserId(commentId, user.getId());
            return new HttpResponseDto("좋아요 취소!", HttpStatus.UNAUTHORIZED.value());
        } else {
            commentLikeRepository.save(new CommentLike(comment, user));
            return new HttpResponseDto("좋아요 성공!", HttpStatus.UNAUTHORIZED.value());
        }
    }
}
