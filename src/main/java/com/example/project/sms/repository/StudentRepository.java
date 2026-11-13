package com.example.project.sms.repository;

import com.example.project.sms.dto.StudentProjectionDto;
import com.example.project.sms.entity.Student;
import com.example.project.sms.projection.StudentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    boolean existsByEmail(String email);
    Optional<Student> findByEmail(String email);
    @Query("""
    SELECT COUNT(s) FROM Student s
    JOIN s.courses c
    WHERE LOWER(c.courseName) = LOWER(:courseName)
    """)
    Long countStudentByCourse(@Param("courseName") String courseName);

    @Query("""
SELECT s FROM Student s JOIN s.courses c
WHERE s.firstName = :firstName
AND LOWER(c.courseName) = LOWER(:course)
""")
    List<Student> findByFirstNameAndCourse(String firstName,String course);

    @Query(value = "SELECT * FROM student WHERE email = :email",
    nativeQuery= true)
    Optional<Student> findByEmailNative(@Param("email") String email);
    //...updated
    List<StudentProjection> findBy();
    //...updated
    @Query("""
SELECT new com.example.project.sms.dto.StudentProjectionDto
(s.firstName,s.lastName,s.email ) FROM Student s
""")
    List<StudentProjectionDto> getStudentProjection();

}
