package com.ecommerce.rema_baby_crochet.common.exception;


public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}
