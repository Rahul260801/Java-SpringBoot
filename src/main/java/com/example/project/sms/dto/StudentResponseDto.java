package com.example.project.sms.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StudentResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private AddressResponseDto addressResponseDto;
    private String departmentName;
    private List<CourseResponseDto> courses;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String  lastModifiedBy;


    private Long version;
}
