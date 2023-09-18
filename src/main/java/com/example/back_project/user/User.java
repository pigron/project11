package com.example.back_project.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter //인스턴스 변수를 가져옴
@Setter //인스턴스 변수를 대입하거나 수정
@Entity (name = "user")//DB 테이블명 "user"와 매핑
public class User {

    @Id  // 식별자
//    @GeneratedValue(strategy = GenerationType.TABLE) //DB에 맞게 자동 생성
    @Column //DB의 테이블명에 매핑됨 (name='컬럼명')으로 원하는 컬럼과 매핑함
    private Long id; // index 값
    private String username; // 접속 ID값
    private String password; //수정가능
    private String name; //수정가능
    private String email; //수정가능
    private String token;  // 생성된 토큰 저장
    private String state; // 유저 상태 확인
}