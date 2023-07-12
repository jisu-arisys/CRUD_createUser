package com.arisys.createUser.dto;

import com.arisys.createUser.dao.User;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
    public UserDto(User entity) {
       this.id = entity.getId();
       this.name = entity.getName();
       this.age = entity.getAge();
       this.gender = entity.getGender();
       this.job = entity.getJob();
    }

    private String id;
    private String name;
    private Integer age;
    private String gender;
    private String job;
}
