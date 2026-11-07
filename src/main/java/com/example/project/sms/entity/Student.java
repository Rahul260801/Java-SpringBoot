package com.example.project.sms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String Number;
    private String password;
    private LocalDateTime createdAt;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "addrss_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
            @JoinTable(name = "student_courses",joinColumns = @JoinColumn(name = "student_id"),inverseJoinColumns =
            @JoinColumn(name = "course_id"))
    List<Course> courses =new ArrayList<>();

}