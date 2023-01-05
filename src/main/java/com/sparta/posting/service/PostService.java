package com.sparta.posting.service;

import com.sparta.posting.dto.CommentResponseDto;
import com.sparta.posting.dto.PostResponseDto;
import com.sparta.posting.dto.PostRequestDto;
import com.sparta.posting.dto.HttpResponseDto;
import com.sparta.posting.entity.Comment;
import com.sparta.posting.entity.Post;
import com.sparta.posting.entity.User;
import com.sparta.posting.repository.CommentLikeRepository;
import com.sparta.posting.repository.CommentRepository;
import com.sparta.posting.repository.PostLikeRepository;
import com.sparta.posting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service                   // 서비스 레이어, 내부에서 자바 로직을 처리함(해당 클래스를 루트 컨테이너에 빈(Bean) 객체로 생성해주는 어노테이션입니다.)
@RequiredArgsConstructor   //초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다.
public class PostService {
    private final PostRepository postRepository;   //@RequiredArgsConstructor 때문에 초기화 하지 않고도 사용가능
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentService commentService;

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
            List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(post.getId());
            List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
            for (Comment comment : comments) {
                commentResponseDtos.add(new CommentResponseDto(comment,commentLikeRepository.countCommentLikesByCommentId(comment.getId())));
            }
            postResponseDtos.add(new PostResponseDto(post,commentResponseDtos, postLikeRepository.countPostLikesByPostId(post.getId())));
        }
        return postResponseDtos;
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPostingById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("게시물이 존재하지 않습니다.")
        );
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(post.getId());
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentResponseDtos.add(new CommentResponseDto(comment,commentLikeRepository.countCommentLikesByCommentId(comment.getId())));
        }
        return new PostResponseDto(post,commentResponseDtos, postLikeRepository.countPostLikesByPostId(post.getId()));
    }

    @Transactional
    public Object update(Long postId, PostRequestDto postRequestDto, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("게시물이 존재하지 않습니다.")
        );
        if(post.getUser().getId() != user.getId()) {
            return new HttpResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.UNAUTHORIZED.value());
        }
        post.update(postRequestDto);
        return new PostResponseDto(post, postLikeRepository.countPostLikesByPostId(post.getId()));
    }

    @Transactional
    public HttpResponseDto deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("게시물이 존재하지 않습니다.")
        );
        if (post.getUser().getId() != user.getId()) {
            return new HttpResponseDto("작성자만 삭제할 수 있습니다.",HttpStatus.UNAUTHORIZED.value());
        }
        commentService.deleteByPost(post);
        postRepository.deleteById(postId);
        return new HttpResponseDto("게시물삭제가 완료되었습니다.",HttpStatus.UNAUTHORIZED.value());
    }

    @Transactional
    public HttpResponseDto deletePostByUser(User user) {
        List<Post> posts = postRepository.findAllByUser(user);
        for(Post post : posts) {
            commentService.deleteByPost(post);
            postRepository.delete(post);
        }
        return new HttpResponseDto("게시물삭제 성공!",HttpStatus.UNAUTHORIZED.value());
    }
}
