package com.example.project.sms.exception;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException (String msg){
        super(msg);
    }
}
