package com.sparta.posting.controller;

import com.sparta.posting.dto.HttpResponseAdminDto;
import com.sparta.posting.dto.PostRequestDto;
import com.sparta.posting.dto.PostResponseAdminDto;
import com.sparta.posting.entity.UserRoleEnum;
import com.sparta.posting.service.PostAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController   //@Controller에 @ResponseBody가 추가된것  주용도는 Json형태로(@ResponseBody를 감싼형태로) 객체 데이터를  반환하는것
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Secured(UserRoleEnum.Authority.ADMIN)
public class PostAdminController {
    private final PostAdminService postAdminService;

    @PutMapping("/post/{postId}")
    @Secured(UserRoleEnum.Authority.ADMIN)
    public PostResponseAdminDto updatePosting(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {
        return postAdminService.update(postId, postRequestDto);
    }

    @DeleteMapping("/post/{postId}")
    public HttpResponseAdminDto deletePosting(@PathVariable Long postId) {
        return postAdminService.deletePosting(postId);
    }
}
