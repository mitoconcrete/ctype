package com.sparta.posting.controller;

import com.sparta.posting.dto.CommentRequestDto;
import com.sparta.posting.dto.ResponseDto;
import com.sparta.posting.entity.Comment;
import com.sparta.posting.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postings")
public class CommentController {
    private final CommentService commentService

    @PostMapping("/{postingId}")
    public Comment addComment(@PathVariable Long postingId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) throws ResponseDto {
        return commentService.addComment(postingId,commentRequestDto,request);
    }
    @PutMapping("/{postingId}/comment/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) throws ResponseDto {
        return commentService.update(id,commentRequestDto,request);
    }
    @DeleteMapping("/{postingId}/comment/{id}")
    public ResponseDto deleteComment(@PathVariable Long id, HttpServletRequest request) throws ResponseDto {
        return commentService.delete(id,request);
    }
}
