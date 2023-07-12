package org.arisys.user_registration.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.arisys.user_registration.dto.PageRequestDTO;
import org.arisys.user_registration.dto.PageResultDTO;
import org.arisys.user_registration.dto.UserDTO;
import org.arisys.user_registration.entity.QUsersEntity;
import org.arisys.user_registration.entity.UsersEntity;
import org.arisys.user_registration.repository.UserlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor //의존성 자동주입
public class UserServiceImp implements UserService {

    private final UserlistRepository userlistRepository;

    //@RequiredArgsConstructor 은 final이나 @NotNull 이붙은  필드의 생성자를
    // 자동으로 의존성 주입을 해주기 떄문에 final로 선언
    @Override
    public Long register(UserDTO userDTO) {

        log.info("========register() 호출 로그========");

        UsersEntity usersEntity = dtoToEntity(userDTO);

        log.info("///==== usersEntity =====///" + usersEntity);

        userlistRepository.save(usersEntity);

        return userDTO.getUserId();
    }

    //목록처리
    @Override
    public PageResultDTO<UserDTO, UsersEntity> getList(PageRequestDTO pageRequestDTO) {
        log.info("===============UserServiceImp- getList() 호출로그==============");

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("userId").descending());
        log.info("pageable : " + pageable);

        //검색 조건 처리, QueryDSL 사용
        BooleanBuilder booleanBuilder = getSearch(pageRequestDTO);

        Page<UsersEntity> result = userlistRepository.findAll(booleanBuilder, pageable);
/*  QueryDSL 사용전
        Page<UsersEntity> result = userlistRepository.findAll(pageable);
        log.info("result : " + result);
*/

        Function<UsersEntity, UserDTO> fn = (entity -> entityToDTO(entity));
        log.info("fn : " + fn);

        return new PageResultDTO<>(result, fn);
    }

    //수정

    @Override
    public void modify(UserDTO userDTO) {

    }

    //삭제
    @Override
    public void remove(Long userId) {
        userlistRepository.deleteById(userId);
    }

    //업데이트
    @Override
    public void updateUser(Long userId, UserDTO userDTO) {
        UsersEntity updateUserEntity = userlistRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        updateUserEntity.updateUserE(userDTO);
        log.info("updateUserEntity : " + updateUserEntity);
        userlistRepository.save(updateUserEntity);

    }

    //검색 메서드 쿼리 dsl 처리
    private BooleanBuilder getSearch(PageRequestDTO pageRequestDTO) {

        String type = pageRequestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QUsersEntity qUsersEntity = QUsersEntity.usersEntity;

        String keyword = pageRequestDTO.getKeyword();

        BooleanExpression booleanExpression = qUsersEntity.userId.gt(0L);
        //gt ==  > ,it ==  <
        //userId > 0 조건만 생성

        booleanBuilder.and(booleanExpression);

        //검색 조건 없는 경우 조건문
        if (type == null || type.trim().length() == 0) {
            return booleanBuilder;
        }


        //검색 조건 작성
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (type.contains("n")) {
            conditionBuilder.or(qUsersEntity.name.contains(keyword));
        }

        if (type.contains("g")) {
            conditionBuilder.or(qUsersEntity.gender.contains(keyword));
        }

        if (type.contains("j")) {
            conditionBuilder.or(qUsersEntity.job.contains(keyword));
        }

        //모든 조건 통합
        booleanBuilder.and(conditionBuilder);



        return booleanBuilder;

    }






}
