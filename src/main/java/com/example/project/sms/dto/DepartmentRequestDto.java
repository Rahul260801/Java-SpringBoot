package com.example.project.sms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentRequestDto {
    @NotBlank(message = "dept name is required")
    @Size(max = 100,message = "dept name cannot exceed 100 character")
    private String departmentName;
}
