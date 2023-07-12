package com.arisys.createUser.common;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserIdIsExistException extends RuntimeException{
    public UserIdIsExistException(String message){super(message);}
}
