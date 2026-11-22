package com.example.project.sms.service;

import com.example.project.sms.dto.StudentResponseDto;
import com.example.project.sms.entity.Address;
import com.example.project.sms.entity.Course;
import com.example.project.sms.entity.Department;
import com.example.project.sms.entity.Student;
import com.example.project.sms.exception.StudentNotFoundException;
import com.example.project.sms.repository.CourseRepository;
import com.example.project.sms.repository.DepartmentRepository;
import com.example.project.sms.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//update
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StudentServiceImplIntegrationTest {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentService studentService;

    @Test
    void shouldReturnStudentById(){
        Department dept = new Department();
        dept.setDeptName("cs");//prev it wasjava object and doesn't save to db
        dept = departmentRepository.save(dept); //save to actual h2 db

        Course course = new Course();
        course.setCourseName("SB");
        course.setDuration("6 month");
        course.setFees(250000.0);
        course.setInstructorName("Abu Bakar");
        course=courseRepository.save(course);

        Address address = new Address();
        address.setCity("Indore");
        address.setState("Mp");
        address.setCountry("Ind");

        Student stu=new Student();
        stu.setFirstName("Pooja");
        stu.setLastName("Rao");
        stu.setEmail("pooja@gmail.com");
        stu.setPassword("Pooja@123");
        stu.setAddress(address);
        stu.setDepartment(dept);
        stu.setCourses(List.of(course));

        Student savedStud=studentRepository.save(stu);

        //Act
        StudentResponseDto result=studentService.getId(savedStud.getId());

        assertEquals(savedStud.getId(),result.getId());
        assertEquals("Ruchi",result.getFirstName());
        assertEquals(1,result.getCourses().size());
        assertEquals("SBA",result.getCourses().get(0).getCourseName());
    }

    @Test
    void shouldThrowExceptionWhenStuNotFound(){
        assertThrows(StudentNotFoundException.class,()->studentService.getId(9999L));
    }

}
