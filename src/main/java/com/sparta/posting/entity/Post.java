package com.sparta.posting.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.posting.dto.PostRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Getter             //접근자 생성-클래스에 선언하면 모든 필드에 적용된다.
@Entity                //DB의 테이블과 일대일로 매칭되는 객체 단위
@NoArgsConstructor    //파라미터가 없는 기본생성자를 만들어 준다.
public class Post extends Datestamped{
    @Id                //테이블 상의 Primary Key와 같은 의미를 가진다.
    @GeneratedValue(strategy = GenerationType.AUTO)     //자동으로 고유 id값을 생성 해주는거 같다.
    @JsonIgnore
    private Long id;

    @Column(nullable = false)         //@Entity 안에서 생성하는 DB테이블의 필드를 만들어 준다. (nullable = false)는 필드값으로 null이 불가능하고 반드시 값이 들어가야함을 나타낸다.
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    @JsonIgnore
    private Long userId;

<<<<<<< HEAD
    private int likecnt = 0;
=======
>>>>>>> feature/commentlike

    @OneToMany
    public List<Comment> comments = new ArrayList<>();

    public Post(PostRequestDto postRequestDto, User user) {               //Posting을 만들떄는 모든 변수에 값을 넣어야 한다.
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
        this.writer = user.getUsername();
        this.userId = user.getId();
    }

    public void update(PostRequestDto postRequestDto) {           //수정할때는 제목,내용만 변경가능하다.
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
    }

    public void addcomment(Comment comment) {
        this.comments.add(comment);
    }
    public void removecomment(Comment comment) {
        this.comments.remove(comment);
    }

<<<<<<< HEAD
    public void likeplus() {
        this.likecnt += 1;
    }
    public void likeminus() {
        this.likecnt -= 1;
    }
=======

>>>>>>> feature/commentlike
}
