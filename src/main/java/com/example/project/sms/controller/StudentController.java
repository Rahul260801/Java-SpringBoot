package com.example.project.sms.controller;

import com.example.project.sms.dto.StudentRequestDto;
import com.example.project.sms.dto.StudentResponseDto;
import com.example.project.sms.entity.Student;
import com.example.project.sms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<StudentResponseDto> getById(@PathVariable Long id) {
//       StudentResponseDto student =studentService.getId(id);
//        if (student==null){
//            return ResponseEntity.notFound().build();
//        }
       // return ResponseEntity.ok(student);
        return ResponseEntity.ok(studentService.getId(id));
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<StudentResponseDto>> getAll(){
        return ResponseEntity.ok(studentService.getAll());
    }
    @PostMapping
    public ResponseEntity<StudentResponseDto> createStud(@Valid @RequestBody StudentRequestDto dto){
        StudentResponseDto saveStud=studentService.saveStud(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveStud);
    }
    @PutMapping("/update")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable Long id,@Valid @RequestBody StudentRequestDto dto){
//        StudentResponseDto student = studentService.updateStud(id,dto);
//        if (student==null){
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(student);
        return ResponseEntity.ok(studentService.updateStud(id,dto));
    }
    @PatchMapping("/partUpdate/{id}")
    public ResponseEntity<StudentResponseDto> partialUpdate(@RequestBody StudentRequestDto dto,@PathVariable Long id){
//        StudentResponseDto student =studentService.partUpdate(dto,id);
//        if (student==null){
//            return ResponseEntity.noContent().build();
//        }
  //      return ResponseEntity.ok(student);
        return ResponseEntity.ok(studentService.partUpdate(dto,id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
