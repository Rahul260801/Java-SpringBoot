package com.example.project.sms.controller;

import com.example.project.sms.dto.DepartmentRequestDto;
import com.example.project.sms.dto.DepartmentResponseDto;
import com.example.project.sms.payload.ApiResponse;
import com.example.project.sms.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> createDepartment(@Valid @RequestBody DepartmentRequestDto dto){
        DepartmentResponseDto response = departmentService.createDept(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
           true,
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "department created successfully",
                response
        ));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getAllDept (){
        List<DepartmentResponseDto> response = departmentService.getAllDept();
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Dept fetch successfully",
                        response
                )
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> getDeptById(@PathVariable Long id){
        DepartmentResponseDto response = departmentService.getDeptById(id);
        ApiResponse<DepartmentResponseDto> apiResponse = new ApiResponse<>(
          true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Dept fetch successfully",
                response
        );
        return ResponseEntity.ok(apiResponse);
    }

}

