package com.arisys.createUser.service;

import com.arisys.createUser.common.UserIdIsExistException;
import com.arisys.createUser.common.UserIsEmptyException;
import com.arisys.createUser.dao.User;
import com.arisys.createUser.dao.UsersRepository;
import com.arisys.createUser.dto.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsersServiceImpl implements UsersService{

    private final UsersRepository repository;

//    @Override
//    public List<User> categorySearchList(String search, String category) {
//        List<User> users = null;
//        if(category.equals("id")) {
//            users = repository.findAllByIdContains(search);
//        } else if(category.equals("name")){
//            users = repository.findAllByNameContains(search);
//        }else{
//            users = repository.findAllByKeyword(search);
//        }
//        return users;
//    }

    @Override
    public List<User> searchConditionAll(SearchCondition search) {
        List<User> users = null;
        if(search.getCategory().equals("id")) {
            users = repository.findAllByIdContains(search.getSort(), search.getSearch());
        } else if(search.getCategory().equals("name")){
            users = repository.findAllByNameContains(search.getSort(), search.getSearch());
        }else{
            users = repository.findAllByKeyword(search.getSort(), search.getSearch());
        }
        return users;
    }

    @Override
    public Page<User> searchCondition(SearchCondition search) {
        Page<User> users = null;
        if(search.getCategory().equals("id")) {
            users = repository.findByIdContains(search.getPageable(), search.getSearch());
        } else if(search.getCategory().equals("name")){
            users = repository.findByNameContains(search.getPageable(), search.getSearch());
        }else{
            users = repository.findByKeyword(search.getPageable(), search.getSearch());
        }
        return users;    }

//    @Override
//    public Page<User> categorySearchPagingList(Pageable pageable, String search, String category) {
//        Page<User> users = null;
//        if(category.equals("id")) {
//            users = repository.findByIdContains(pageable, search);
//        } else if(category.equals("name")){
//            users = repository.findByNameContains(pageable, search);
//        }else{
//            users = repository.findByKeyword(pageable, search);
//        }
//        return users;
//    }

//    @Override
//    public Page<User> searchPagingList(Pageable pageable, String search) {
//        Page<User> users = repository.findByKeyword(pageable, search);
//        return users;
//    }

    @Override
    public Page<User> pagingList(Pageable pageable) {
        Page<User> users = repository.findAll(pageable);

        return users;
    }

    public List<User> list()throws UserIsEmptyException{
        List<User> users = repository.findAll();
        if(users == null){
            throw new UserIsEmptyException();
        }

        return users;
    }
    public User read(String id)throws UserIsEmptyException{
        Optional<User> opUser = repository.findById(id);

        User user = opUser.orElseGet(() ->{
            String message = "User with ID " + id + " not found";
            throw new UserIsEmptyException(message);
        });
        return user;
    }

    public void insert(User user){
        //존재하는 id 조건처리
       if(repository.existsById(user.getId())){
           String message = "User with ID " + user.getId() + " is exist";
           throw new UserIdIsExistException(message);
       }
        repository.save(user);
    }
    public void update(User user){
        //존재하지 않는 id 조건처리
        User changeUser = this.read(user.getId());
        //변경할 값만 받은경우 or 지정한 값만 변경하기 위한 조건처리
        changeUser.setName(user.getName());
        changeUser.setAge(user.getAge());
        changeUser.setGender(user.getGender());
        changeUser.setJob(user.getJob());

        repository.save(changeUser);
    }

    public void delete(String id){
        repository.deleteById(id);
    }

}
