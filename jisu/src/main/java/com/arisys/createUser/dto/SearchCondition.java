package com.arisys.createUser.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

@AllArgsConstructor
@ToString
@Setter
@Getter
@NoArgsConstructor
public class SearchCondition implements Serializable {
//실행시점에서 JVM이 serialVersionUID 디폴트 값을 산정
    private Integer page = 1;
    private String search;
    private String category;
    private String sending;
    private String sortType;
    private final Integer pageSize = 3;

    public Sort getSort(){
        if(this.sending.toUpperCase().equals("ASC")){
            return Sort.by(Sort.Direction.ASC, this.sortType);
        }else {
            return Sort.by(Sort.Direction.DESC, this.sortType);
        }
    }

    public Pageable getPageable(){
        return PageRequest.of(this.page, this.pageSize, this.getSort());
    }
}
