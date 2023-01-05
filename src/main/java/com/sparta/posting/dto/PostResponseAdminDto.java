package com.sparta.posting.dto;

import com.sparta.posting.entity.Comment;
import com.sparta.posting.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class PostResponseAdminDto {
    private Long id;
    private String title;
    private String contents;
    private String writer;
    private int likecnt;
    private List<CommentResponseAdminDto> comments = new ArrayList<>();

    public PostResponseAdminDto(Post post,int likecnt) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.writer = post.getUser().getUsername();
        this.likecnt = likecnt;
    }

}
