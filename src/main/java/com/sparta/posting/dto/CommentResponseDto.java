package com.sparta.posting.dto;

import com.sparta.posting.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentResponseDto {
    private String content;
    private String writer;

    public CommentResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.writer = comment.getWriter();
    }
}
