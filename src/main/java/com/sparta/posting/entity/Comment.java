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
    private Long id;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name="writer",nullable = false)
    @ManyToOne
    private User user;

    @JoinColumn(name="postId",nullable = false)
    @ManyToOne
    private Post post;


    public Comment(CommentRequestDto commentRequestDto, User user, Post post) {
        this.content = commentRequestDto.getContent();
        this.user = user;
        this.post = post;
    }


    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }

}
