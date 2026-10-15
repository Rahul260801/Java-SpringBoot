package com.example.project.sms.exception;

public class StudentNotFoundException extends RuntimeException {
   public StudentNotFoundException(String msg){
       super(msg);
   }
}
