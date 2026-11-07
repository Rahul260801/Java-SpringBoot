package com.example.project.sms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDto {
    @NotBlank(message = "Course name req")
    private String courseName;
    private String duration;
    @NotNull(message = "fees is req")
    private Double fees;
    private String instructorName;
}
