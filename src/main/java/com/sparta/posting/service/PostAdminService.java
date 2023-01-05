package com.sparta.posting.service;

import com.sparta.posting.dto.*;
import com.sparta.posting.entity.Post;
import com.sparta.posting.repository.PostLikeRepository;
import com.sparta.posting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostAdminService {
    private final PostRepository postRepository;   //@RequiredArgsConstructor 때문에 초기화 하지 않고도 사용가능
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostResponseAdminDto update(Long postId, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("게시물이 존재하지 않습니다.")
        );
        post.update(postRequestDto);
        return new PostResponseAdminDto(post, postLikeRepository.countPostLikesByPostId(postId));
    }

    @Transactional
    public HttpResponseAdminDto deletePosting(Long postId)  {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("게시물이 존재하지 않습니다.")
        );
        postRepository.delete(post);
        return new HttpResponseAdminDto("게시물삭제가 완료되었습니다.",HttpStatus.UNAUTHORIZED.value());
    }
}
