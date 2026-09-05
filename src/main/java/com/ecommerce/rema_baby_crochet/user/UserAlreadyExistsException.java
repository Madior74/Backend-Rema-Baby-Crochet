package com.ecommerce.rema_baby_crochet.user;


public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("Email already in use");
    }
}
