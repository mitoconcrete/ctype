package com.sparta.posting.entity;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@Getter
@MappedSuperclass  //엔티티 별로 공통 필드가 존재하는 경우 불 필요한 중복 코드를 제거하기 위해 사용(Posting에서 상속하면 Datestamped 내부변수가 자동으로 필드생성)
@EntityListeners(AuditingEntityListener.class)  //audit을 이용하면 자동으로 시간을 매핑하여 데이터베이스의 테이블에 넣어주게 된다
public abstract class Datestamped {

    @CreatedDate                //entity가 생성되어 DB에 저장될때 시간이 자동 저장된다.
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;
}
