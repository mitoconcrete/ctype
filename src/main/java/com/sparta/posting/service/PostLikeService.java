package com.sparta.posting.service;

import com.sparta.posting.dto.HttpResponseDto;
import com.sparta.posting.entity.PostLike;
import com.sparta.posting.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    @Transactional
    public HttpResponseDto toggleLike(Long postId, Long userId) {
        if(postLikeRepository.findByPostIdAndUserId(postId, userId).isPresent()) {
            postLikeRepository.deleteByPostIdAndUserId(postId,userId);
            return new HttpResponseDto("좋아요 취소!", HttpStatus.UNAUTHORIZED.value());
        } else {
            postLikeRepository.save(new PostLike(postId,userId));
            return new HttpResponseDto("좋아요 성공!", HttpStatus.UNAUTHORIZED.value());
        }
    }
}
