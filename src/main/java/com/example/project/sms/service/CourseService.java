package com.example.project.sms.service;

import com.example.project.sms.dto.CourseRequestDto;
import com.example.project.sms.dto.CourseResponseDto;

import java.util.List;

public interface CourseService {
    CourseResponseDto createCourse (CourseRequestDto requestDto);
    List<CourseResponseDto> getAll();
    CourseResponseDto getById(Long id);
}
