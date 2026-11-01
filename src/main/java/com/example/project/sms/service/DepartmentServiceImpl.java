package com.example.project.sms.service;

import com.example.project.sms.dto.DepartmentRequestDto;
import com.example.project.sms.dto.DepartmentResponseDto;
import com.example.project.sms.dto.StudentResponseDto;
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
        return MapToResponse(saveDept);
    }

    @Override
    public List<DepartmentResponseDto> getAllDept() {
        return departmentRepository.findAll().stream().map(this::MapToResponse).toList();
    }

    @Override
    public DepartmentResponseDto getDeptById(Long id) {
        Department dept = departmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("dept name found"));
        return MapToResponse(dept);

    }
    private DepartmentResponseDto MapToResponse(Department department){
        DepartmentResponseDto dto =new DepartmentResponseDto();
        dto.setId(department.getId());
        dto.setDepartmentName(department.getDeptName());
        List<StudentResponseDto> stu=department.getStudents().stream().map(
                student -> {
                    StudentResponseDto studentdto= new StudentResponseDto();
                    studentdto.setId(student.getId());
                    studentdto.setFirstName(student.getFirstName());
                    studentdto.setLastName(student.getLastName());
                    studentdto.setEmail(student.getEmail());
                    return studentdto;
                }
        ).toList();
        dto.setStudents(stu);
        return dto;
    }
}
