package com.arisys.createUser.service;

import com.arisys.createUser.dao.User;
import com.arisys.createUser.dto.SearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsersService {
    List<User> searchConditionAll(SearchCondition search);
    Page<User> searchCondition(SearchCondition search);
    Page<User> pagingList(Pageable pageable);
    List<User> list();
    User read(String id);
    void insert(User user);
    void update(User user);
    void delete(String id);

}
