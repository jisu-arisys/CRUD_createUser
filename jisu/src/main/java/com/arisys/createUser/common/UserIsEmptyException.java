package com.arisys.createUser.common;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserIsEmptyException extends RuntimeException{
    public UserIsEmptyException(String message){super(message);}
}
