package com.sparta.posting.controller;

import com.sparta.posting.dto.PostRequestDto;
import com.sparta.posting.entity.Post;
import com.sparta.posting.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//view를 반환할때는 @Controller 사용해야함
@RestController   //@Controller에 @ResponseBody가 추가된것  주용도는 Json형태로(@ResponseBody를 감싼형태로) 객체 데이터를  반환하는것
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/api")
    //핸들러가 어떤 방식(Get,Post...)으로 컨트롤러에 요청을 하느냐에따라 구분해서 매서드를 실행시키기 위해 Mapping을 사용한다.
    public ModelAndView home() {
        return new ModelAndView();
    }

    @PostMapping("/api/postings")
    public Post createPosting(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request) throws ResponseDto { //Body 형태로 포스팅 정보를 받아오고 jwt도 같이 받아온다.
        return postService.createPosting(postRequestDto, request);
    }

    @GetMapping("/api/postings")
    public List<Post> getPostings() {
        return postService.getPostings();
    }

    @GetMapping("/api/postings/{id}")
    public Post getPostingsById(@PathVariable Long id) throws ResponseDto {
        return postService.getPostingById(id);
    }

    @PutMapping("/api/postings/{id}")
    public Post updatePosting(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, HttpServletRequest request) throws ResponseDto {
        return postService.update(id, postRequestDto,request);         //jwt로 검증해서 수정이 이루어진다.
    }

    @DeleteMapping("/api/postings/{id}")
    public ResponseDto deletePosting(@PathVariable Long id, HttpServletRequest request) throws ResponseDto {
        return postService.deletePosting(id,request);
    }
}
