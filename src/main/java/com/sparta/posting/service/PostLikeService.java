package com.sparta.posting.service;

import com.sparta.posting.dto.HttpResponseDto;
import com.sparta.posting.entity.Post;
import com.sparta.posting.entity.PostLike;
import com.sparta.posting.entity.User;
import com.sparta.posting.repository.PostLikeRepository;
import com.sparta.posting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    @Transactional
    public HttpResponseDto toggleLike(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("게시물이 존재하지 않습니다.")
        );
        if(postLikeRepository.findByPostIdAndUserId(postId, user.getId()).isPresent()) {
            postLikeRepository.deleteByPostIdAndUserId(postId,user.getId());
            return new HttpResponseDto("좋아요 취소!", HttpStatus.UNAUTHORIZED.value());
        } else {
            postLikeRepository.save(new PostLike(post,user));
            return new HttpResponseDto("좋아요 성공!", HttpStatus.UNAUTHORIZED.value());
        }
    }
}