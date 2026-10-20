package com.example.project.sms.service;

import com.example.project.sms.dto.DepartmentRequestDto;
import com.example.project.sms.dto.DepartmentResponseDto;

import java.util.List;

public interface DepartmentService {
    DepartmentResponseDto createDept(DepartmentRequestDto requestDto);
    List<DepartmentResponseDto> getAllDept();
    DepartmentResponseDto getDeptById(Long id);

}
