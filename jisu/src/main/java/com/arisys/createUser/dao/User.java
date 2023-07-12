package com.arisys.createUser.dao;

//jakarta.persistence : java에서 entity관련 sql문을 작성해줌 = JPQL
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of="id")
@ToString
@Entity
@Table(name="users")
//해당 어노테이션을 생략하게 되면 엔티티클래스 이름을 테이블 이름으로 매핑한다.
public class User {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String job;

//대소문자를 구분하는 데이터베이스를 사용하게 되면 @Column(name=”AGE”)와 같이 명시적으로 매핑해야 한다.


}
