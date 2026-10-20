package com.example.project.sms.service;

import com.example.project.sms.dto.DepartmentRequestDto;
import com.example.project.sms.dto.DepartmentResponseDto;
import com.example.project.sms.entity.Department;
import com.example.project.sms.exception.ResourceNotFoundException;
import com.example.project.sms.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    @Override
    public DepartmentResponseDto createDept(DepartmentRequestDto requestDto) {
        Department dept = new Department();
        dept.setDeptName(requestDto.getDepartmentName());
        Department saveDept = departmentRepository.save(dept);
        return new DepartmentResponseDto(
                saveDept.getId(),saveDept.getDeptName()
        );
    }

    @Override
    public List<DepartmentResponseDto> getAllDept() {
        return departmentRepository.findAll().stream().map(department -> new DepartmentResponseDto(
                department.getId(),department.getDeptName()
        )).toList();
    }

    @Override
    public DepartmentResponseDto getDeptById(Long id) {
        Department dept = departmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("dept name found"));
        return new DepartmentResponseDto(dept.getId(),dept.getDeptName());

    }
}
