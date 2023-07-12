package com.arisys.createUser.controller;

import com.arisys.createUser.common.CreateExcel;
import com.arisys.createUser.dao.User;
import com.arisys.createUser.dto.SearchCondition;
import com.arisys.createUser.dto.UserDto;
import com.arisys.createUser.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@RestControllerAdvice
//예외처리 핸들러 GlobalExceptionHandler 클래스가 작동 및 JSON 형식의 응답을 생성할 수 있게 활성화 함
public class UsersController {
    public static final Logger logger = LogManager.getLogger(UsersController.class);
    private final UsersService service;
    private final String savePath = "C:\\Users\\USER\\Downloads\\";
    private final String fileName = "searchUserList.xlsx";

    //downloadExcel
    @PostMapping("/excel")
    public ResponseEntity<byte[]> downloadExcel(@RequestBody SearchCondition search) {
        List<User> users = service.searchConditionAll(search);
        logger.info("List : 검색목록엑셀출력" + users.size());

        byte[] fileBytes = new byte[0];
        try {
            fileBytes = CreateExcel.writeUserListToFile(savePath + fileName, users, search);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("CreateExcel 실패");
        }

        return new ResponseEntity<>(fileBytes, HttpStatus.OK);
    }

//    //검색 카테고리 별 조회
//    @GetMapping("/category/{page}/{search}/{category}/{sort}/{sending}")
//    public ResponseEntity<Page<User>> getListWithPaging(@PathVariable("page") Integer page,
//            @PathVariable("search") String search, @PathVariable("category") String category,
//            @PathVariable("sort") String sort, @PathVariable() String sending) {
//        page -= 1;
//        Pageable pageable = PageRequest.of(page, pageSize, createSort(sending,sort));
//        Page<User> users = service.categorySearchPagingList(pageable, search, category);
//        logger.info("List : 카테고리검색페이지출력" + page);
//
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
    //검색 카테고리 별 조회
    @PostMapping("/searchCondition")
    public ResponseEntity<Page<User>> getListWithPaging(@RequestBody SearchCondition search) {
        Page<User> users = service.searchCondition(search);
        logger.info("List : 컨디션검색페이지출력", search.getPage());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //페이징목록 조회
    @GetMapping("/page/{page}")
    public ResponseEntity<Page<User>> getListWithPaging(@PathVariable("page") Integer page) {
        page -= 1;
        Pageable pageable = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "id"));
        Page<User> users = service.pagingList(pageable);
        logger.info("List : 페이지출력" + page);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

//    //목록조회
//    @GetMapping("/list")
//    public ResponseEntity<List<UserDto>> getList(){
//        logger.info("list : 목록출력");
//        List<UserDto> users = getUsersDto(service.list());
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }

    private List<UserDto> getUsersDto(List<User> users){
        List<UserDto> usersDto = new ArrayList<>();

        for (User userEntity : users) {
            UserDto userDto = new UserDto(userEntity);
            usersDto.add(userDto);
        }
        return usersDto;
    }

    //id값 하나 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> read(@PathVariable String id){
        User user = service.read(id);
        logger.info("아이디 조회" + id);
        return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
    }

    //id 일치하지 않으면 등록, 전달받은 값 리턴
    @PostMapping("/")
    public ResponseEntity<UserDto> insert(@Validated @RequestBody User user){


        service.insert(user);
        return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
    }

    //id 일치하면 나머지 전체 수정, 전달받은 값 리턴
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable String id, @Validated @RequestBody User user){
        user.setId(id);
        service.update(user);
        return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
    }

    //id 일치하면 삭제, 반환값 없음
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        service.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
