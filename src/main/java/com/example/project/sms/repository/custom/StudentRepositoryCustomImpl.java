package com.example.project.sms.repository.custom;

import com.example.project.sms.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

//updated
public class StudentRepositoryCustomImpl implements StudentRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Student> findStudentCustom() {
        TypedQuery<Student> studentTypedQuery = entityManager.createQuery(
                "SELECT s FROM Student s",Student.class) ;
        return studentTypedQuery.getResultList();
    }

    @Override
    public List<Student> findStudentByDept(String departmentName) {
        TypedQuery<Student> query =entityManager.createQuery(
          "SELECT s FROM Student s WHERE s.department.deptName = :departmentName",Student.class
        );
        query.setParameter("departmentName",departmentName);
        return query.getResultList();
    }
}
