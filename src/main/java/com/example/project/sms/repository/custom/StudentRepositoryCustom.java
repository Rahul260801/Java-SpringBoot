package com.example.project.sms.repository.custom;

import com.example.project.sms.entity.Student;

import java.util.List;

//updated
public interface StudentRepositoryCustom {
    List<Student> findStudentCustom();
    List<Student> findStudentByDept(String departmentName);
}
