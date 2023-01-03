package com.sparta.posting.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.posting.dto.CommentRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor
public class Comment extends Datestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    @JoinColumn(name="writer",nullable = false)
    @JsonIgnore
    private Long userId;

    @JoinColumn(name="postId",nullable = false)
    @JsonIgnore
    private Long postId;


    public Comment(CommentRequestDto commentRequestDto, User user, Long id) {
        this.content = commentRequestDto.getContent();
        this.writer = user.getUsername();
        this.userId = user.getId();
        this.postId = id;
    }


    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
}
