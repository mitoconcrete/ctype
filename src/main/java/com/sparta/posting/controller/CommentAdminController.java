package com.sparta.posting.controller;

import com.sparta.posting.dto.*;
import com.sparta.posting.entity.UserRoleEnum;
import com.sparta.posting.service.CommentAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/post")
@Secured(UserRoleEnum.Authority.ADMIN)
public class CommentAdminController {
    private final CommentAdminService commentAdminService;

    @PutMapping("/{postId}/comment/{commentId}")
    public CommentResponseAdminDto updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto) throws HttpResponseAdminDto {
        return commentAdminService.update(commentId,commentRequestDto);
    }
    @DeleteMapping("/{postId}/comment/{commentId}")
    public HttpResponseAdminDto deleteComment(@PathVariable Long commentId) throws HttpResponseAdminDto {
        return commentAdminService.delete(commentId);
    }
}