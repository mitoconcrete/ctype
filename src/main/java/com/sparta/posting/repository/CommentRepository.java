package com.sparta.posting.repository;

import com.sparta.posting.entity.Comment;
import com.sparta.posting.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    //Optional<Comment> findByPostingId(Long id);

    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postingId);
}
