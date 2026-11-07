package com.example.project.sms.controller;

import com.example.project.sms.dto.CourseRequestDto;
import com.example.project.sms.dto.CourseResponseDto;
import com.example.project.sms.payload.ApiResponse;
import com.example.project.sms.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponseDto>> createCourse(@Valid @RequestBody CourseRequestDto dto){
        CourseResponseDto response = courseService.createCourse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Course created success",
                response
        ));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponseDto>>> getAll(){
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "All Course fetch success",
                        courseService.getAll()
                )
        );
    }
    @GetMapping("/{id}")
    private ResponseEntity<ApiResponse<CourseResponseDto>>getById(@PathVariable Long id){
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Course fetch success",
                        courseService.getById(id)
                )
        );
    }
}
