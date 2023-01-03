package com.sparta.posting.service;

import com.sparta.posting.dto.HttpResponseDto;
import com.sparta.posting.entity.CommentLike;
import com.sparta.posting.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public HttpResponseDto toggleLike(Long commentId, Long userId) {
        if (commentLikeRepository.findByCommentIdAndUserId(commentId, userId).isPresent()) {
            commentLikeRepository.deleteByCommentIdAndUserId(commentId, userId);
            return new HttpResponseDto("좋아요 취소!", HttpStatus.UNAUTHORIZED.value());
        } else {
            commentLikeRepository.save(new CommentLike(commentId, userId));
            return new HttpResponseDto("좋아요 성공!", HttpStatus.UNAUTHORIZED.value());
        }
    }
}
