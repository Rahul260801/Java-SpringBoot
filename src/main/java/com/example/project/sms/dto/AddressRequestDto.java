package com.example.project.sms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequestDto {
    @NotBlank(message = "city is req")
    private String city;
    private String state;
    private String country;
}
