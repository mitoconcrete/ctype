package com.sparta.posting.repository;

import com.sparta.posting.entity.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndUserId(Long id, Long userId);

    List<Post> findAllByOrderByCreatedAtDesc();  //findAllByOrderByColumnDesc (Column에 원하는 값을 적으면 그 기준으로 내림차순 정렬해준다)

}
