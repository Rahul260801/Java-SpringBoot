package com.example.project.sms.dto;

import lombok.Data;

@Data
public class StudentResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private AddressResponseDto addressResponseDto;
}
