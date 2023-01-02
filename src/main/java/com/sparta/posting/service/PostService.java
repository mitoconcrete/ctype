package com.sparta.posting.service;

import com.sparta.posting.dto.PostResponseDto;
import com.sparta.posting.dto.PostRequestDto;
import com.sparta.posting.dto.HttpResponseDto;
import com.sparta.posting.entity.*;
import com.sparta.posting.repository.CommentRepository;
import com.sparta.posting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service                   // 서비스 레이어, 내부에서 자바 로직을 처리함(해당 클래스를 루트 컨테이너에 빈(Bean) 객체로 생성해주는 어노테이션입니다.)
@RequiredArgsConstructor   //초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다.
public class PostService {
    private final PostRepository postRepository;   //@RequiredArgsConstructor 때문에 초기화 하지 않고도 사용가능
    private final CommentRepository commentRepository;

    @Transactional             //컨트롤러와 결합해주는 역할을 한다.
    public PostResponseDto createPosting(PostRequestDto postRequestDto, User user) {
        Post post = postRepository.save(new Post(postRequestDto,user));
        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)      //읽기전용으로 하면 약간의 성능적인 이점을 얻을 수 있다.
    public List<PostResponseDto> getPostings() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : posts) {
            post.comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(post.getId());
            postResponseDtos.add(new PostResponseDto(post));
        }
        return postResponseDtos;
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPostingById(Long postid) throws HttpResponseDto {
        Post post = postRepository.findById(postid).orElseThrow(
                () -> new HttpResponseDto("게시물이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED.value())
        );
        post.comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(post.getId());
        return new PostResponseDto(post);
    }

    @Transactional
    public Object update(Long postid, PostRequestDto postRequestDto, User user) throws HttpResponseDto {
        Post post = postRepository.findById(postid).orElseThrow(
                () -> new HttpResponseDto("게시물이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED.value())
        );
        if(post.getUserId() != user.getId()) {
            return new HttpResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.UNAUTHORIZED.value());
        }
        post.update(postRequestDto);
        return new PostResponseDto(post);
    }

    @Transactional
    public HttpResponseDto deletePosting(Long postid, User user) throws HttpResponseDto {
        Post post = postRepository.findById(postid).orElseThrow(
                () -> new HttpResponseDto("게시물이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED.value())
        );
        if (post.getUserId() != user.getId()) {
            return new HttpResponseDto("작성자만 삭제할 수 있습니다.",HttpStatus.UNAUTHORIZED.value());
        }
        postRepository.deleteById(postid);
        return new HttpResponseDto("게시물삭제가 완료되었습니다.",HttpStatus.UNAUTHORIZED.value());
    }
}
