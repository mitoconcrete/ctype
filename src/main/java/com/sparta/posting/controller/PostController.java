package com.sparta.posting.controller;

import com.sparta.posting.dto.HttpResponseDto;
import com.sparta.posting.dto.PostRequestDto;
import com.sparta.posting.dto.PostResponseDto;
import com.sparta.posting.entity.Post;
import com.sparta.posting.security.UserDetailsImpl;
import com.sparta.posting.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//view를 반환할때는 @Controller 사용해야함
@RestController   //@Controller에 @ResponseBody가 추가된것  주용도는 Json형태로(@ResponseBody를 감싼형태로) 객체 데이터를  반환하는것
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public PostResponseDto createPosting(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) { //Body 형태로 포스팅 정보를 받아오고 jwt도 같이 받아온다.
        return postService.createPosting(postRequestDto, userDetails.getUser());
    }

    @GetMapping("/post")
    public List<PostResponseDto> getPostings() {
        return postService.getPostings();
    }

    @GetMapping("/post/{postId}")
    public PostResponseDto getPostingsById(@PathVariable Long postId) {
        return postService.getPostingById(postId);
    }

    @PutMapping("/post/{postId}")
    public Object updatePosting(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails)  {
        return postService.update(postId, postRequestDto,userDetails.getUser());
    }

    @DeleteMapping("/post/{postId}")
    public HttpResponseDto deletePosting(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(postId,userDetails.getUser());
    }
}
