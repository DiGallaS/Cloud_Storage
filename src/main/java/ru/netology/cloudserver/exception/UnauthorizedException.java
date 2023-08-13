package ru.netology.cloudserver.exception;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String msg) {
        super(msg);
    }
}
