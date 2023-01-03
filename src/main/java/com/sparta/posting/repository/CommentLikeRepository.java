package com.sparta.posting.repository;

import com.sparta.posting.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);
    Optional<CommentLike>deleteByCommentIdAndUserId(Long commentId, Long userId);
    Integer countCommentLikesByCommentId(Long commentId);
}
