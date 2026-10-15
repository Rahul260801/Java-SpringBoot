package com.example.project.sms.repository;

import com.example.project.sms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    boolean existsByEmail(String email);
}
