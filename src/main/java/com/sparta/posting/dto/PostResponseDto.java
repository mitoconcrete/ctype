package com.sparta.posting.dto;

import com.sparta.posting.entity.Comment;
import com.sparta.posting.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String writer;
    private int likecnt;
    private List<CommentResponseDto> comments = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.writer = post.getUser().getUsername();
        for(Comment comment :post.getComments()){this.comments.add(new CommentResponseDto(comment));}
    }
}
