package com.example.project.sms.controller;

import com.example.project.sms.dto.StudentRequestDto;
import com.example.project.sms.dto.StudentResponseDto;
import com.example.project.sms.entity.Student;
import com.example.project.sms.payload.ApiResponse;
import com.example.project.sms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {
    @Autowired
    public final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getById(@PathVariable Long id) {
        StudentResponseDto students = studentService.getId(id);
        ApiResponse<StudentResponseDto> response = new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Student fetch successfully",
                students
        );
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAll(){
        List<StudentResponseDto> students = studentService.getAll();
        ApiResponse<List<StudentResponseDto>> response = new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Student fetch successfully",
                students
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity <ApiResponse<StudentResponseDto>> createStud(@Valid @RequestBody StudentRequestDto dto){
        StudentResponseDto saveStud=studentService.saveStud(dto);
        ApiResponse <StudentResponseDto> res= new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Student created  successfully",
                saveStud
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
    @PutMapping("/update")
    public ResponseEntity <ApiResponse<StudentResponseDto>> updateStudent(@PathVariable Long id,@Valid @RequestBody StudentRequestDto dto){
        StudentResponseDto stu = studentService.updateStud(id,dto);
        ApiResponse<StudentResponseDto> res=new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Student updated  successfully",
                stu
        );
        return ResponseEntity.ok(res);
    }
    @PatchMapping("/partUpdate/{id}")
    public ResponseEntity <ApiResponse<StudentResponseDto>> partialUpdate(@RequestBody StudentRequestDto dto,@PathVariable Long id){
        StudentResponseDto students = studentService.partUpdate(dto,id);
        ApiResponse<StudentResponseDto> resp = new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Student updated  successfully",
                students
        );
        return ResponseEntity.ok(resp);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity <ApiResponse<Object>> deleteById(@PathVariable Long id){
        studentService.deleteById(id);
         ApiResponse<Object> response =new ApiResponse<>(
                 true,
                 LocalDateTime.now(),
                 HttpStatus.OK.value(),
                 "student deleted successfully",
                 null
         );
        return ResponseEntity.ok(response);
    }
}
