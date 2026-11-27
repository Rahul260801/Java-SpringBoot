package com.example.project.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//update-5
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
}
