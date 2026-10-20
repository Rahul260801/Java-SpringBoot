package com.example.project.sms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentRequestDto {
    @NotBlank(message = "first name required") @Size(min = 2, max = 30, message = "it is between 2 to 40 character")
    private String firstName;
    private String lastName;
    private String Number;
    private String email;
    @NotBlank(message = "password is required") @Size(min=8,message = "Pwd Required") @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$"
    )
    private String password;

    private AddressRequestDto addressRequestDto;

    @NotNull(message = "Dept id req")
    private Long departmentId;
}
