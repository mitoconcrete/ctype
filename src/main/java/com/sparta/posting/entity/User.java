package com.sparta.posting.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter          				//접근가능하게 하기위해
@Entity(name = "users")
@NoArgsConstructor             //파라미터가 필요없는 객체의 기본생성자를 만들어준다.
public class User extends Datestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    //ID 값을 자동으로 만들어준다.
    private Long id;				//중복이 안되는 username으로 대신해도 되지 않을까?

    @Column(nullable = false, unique = true)	//필드에는 @Column을 붙여야한다. 옵션은 null비허용,unique는 중복허용 여부이다.
    private String username;

    @Column(nullable = false)		//
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
