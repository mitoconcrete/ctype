package com.sparta.posting.repository;

import com.sparta.posting.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
    Optional<PostLike>deleteByPostIdAndUserId(Long postId,Long userId);
    Integer countPostLikesByPostId(Long postId);
}
