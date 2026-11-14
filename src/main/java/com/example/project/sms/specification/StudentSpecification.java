package com.example.project.sms.specification;

import com.example.project.sms.entity.Student;
import org.springframework.data.jpa.domain.Specification;

//updated
public class StudentSpecification {
     public static Specification<Student> hasFirstName(String firstName){
         if (firstName==null || firstName.isBlank()){
             return null;
         }
         return (root, query, criteriaBuilder) ->
                 criteriaBuilder.equal(root.get("firstName"),firstName);

     }
     public static Specification<Student> hasEmail(String email){
         if (email ==null || email.isBlank()){
             return null;
         }
         return (root, query, criteriaBuilder) ->
                 criteriaBuilder.equal(root.get("email"),email);
     }
    public static Specification<Student> hasCourse(String courses){
        if (courses ==null || courses.isBlank()){
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("courses").get("courseName"),courses);
    }
}
