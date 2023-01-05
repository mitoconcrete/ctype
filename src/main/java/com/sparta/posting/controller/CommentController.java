package com.sparta.posting.controller;

import com.sparta.posting.dto.CommentRequestDto;
import com.sparta.posting.dto.CommentResponseDto;
import com.sparta.posting.dto.HttpResponseDto;
import com.sparta.posting.entity.Comment;
import com.sparta.posting.security.UserDetailsImpl;
import com.sparta.posting.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}/comment")
    public CommentResponseDto addComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.addComment(postId,commentRequestDto,userDetails.getUser());
    }
    @PutMapping("/{postId}/comment/{commentId}")
    public Object updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.update(commentId,commentRequestDto,userDetails.getUser());
    }
    @DeleteMapping("/{postId}/comment/{commentId}")
    public HttpResponseDto deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.delete(commentId,userDetails.getUser());
    }
}
