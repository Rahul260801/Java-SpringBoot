package com.example.project.sms.service;

import com.example.project.sms.dto.StudentResponseDto;
import com.example.project.sms.entity.Address;
import com.example.project.sms.entity.Department;
import com.example.project.sms.entity.Student;
import com.example.project.sms.exception.StudentNotFoundException;
import com.example.project.sms.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void shouldReturnStudentById(){
        //Arrange
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Usha");
        student.setLastName("Yadav");
        student.setEmail("usha@gmail.com");

        Address address = new Address();
        address.setCity("udaipur");
        address.setState("Raj");
        address.setCountry("Ind");
        student.setAddress(address);

        Department department = new Department();
        department.setDeptName("cs");
        student.setDepartment(department);
        student.setCourses(List.of());
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
      //  when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        //Act
        StudentResponseDto result = studentService.getId(1l);
        //Assert
        assertEquals(1L,result.getId());
        assertEquals("Usha",result.getFirstName());
        assertEquals("Yadav",result.getLastName());
        assertEquals("Usha@gmail.com",result.getEmail());
        verify(studentRepository,times(1)).findById(1L);
        verify(studentRepository,never()).deleteById(1L);
    }
    @Test
    void shouldThrowExceptionWhenRepoFails(){
        when(studentRepository.findById(1L)).thenThrow(new RuntimeException());
     //   studentService.getId(1L);
        RuntimeException exception = assertThrows(RuntimeException.class,()->studentService.getId(1L));
    }
    @Test
    void ShouldThroeExceptionWhenStuNotFound(){
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class,()->studentService.getId(99L));
    }
}
