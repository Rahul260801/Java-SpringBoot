package com.example.project.sms.service;


import com.example.project.sms.dto.StudentRequestDto;
import com.example.project.sms.dto.StudentResponseDto;
import com.example.project.sms.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;

public interface StudentService {
 StudentResponseDto getId(Long id);

    List<StudentResponseDto> getAll();

   StudentResponseDto saveStud(StudentRequestDto dto);

 StudentResponseDto updateStud(Long id, StudentRequestDto dto);

    void deleteById(Long id);

 StudentResponseDto partUpdate(StudentRequestDto dto,Long id);
}
