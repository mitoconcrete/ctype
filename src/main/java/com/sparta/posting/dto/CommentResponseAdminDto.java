package com.sparta.posting.dto;

import com.sparta.posting.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentResponseAdminDto {
    private String content;
    private String writer;

    public CommentResponseAdminDto(Comment comment) {
        this.content = comment.getContent();
        this.writer = comment.getWriter();
    }
}
