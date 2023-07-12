package com.arisys.createUser.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UsersRepository extends JpaRepository<User, String> {

    Page<User> findAll(Pageable pageable);

    @Query("SELECT user FROM User user WHERE user.id LIKE %:search% OR user.name LIKE %:search% ")
    Page<User> findByKeyword(Pageable pageable, String search);
    Page<User> findByIdContains(Pageable pageable,String search);
    Page<User> findByNameContains(Pageable pageable,String search);
    @Query("SELECT user FROM User user WHERE user.id LIKE %:search% OR user.name LIKE %:search% ")
    List<User> findAllByKeyword(Sort sort, String search);
    List<User> findAllByIdContains(Sort sort, String search);
    List<User> findAllByNameContains(Sort sort, String search);
}

