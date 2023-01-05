package com.sparta.posting.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PostLike {
    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "POST_ID",nullable = false)
    @ManyToOne
    private Post post;

    @JoinColumn(name = "USER_ID",nullable = false)
    @ManyToOne
    private User user;
}
