package com.example.project.sms.controller;

import com.example.project.sms.dto.LoginRequest;
import com.example.project.sms.dto.LoginResponse;
import com.example.project.sms.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//update-6
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(//from request usrname and pw come and we wrap to UsernamePasswordAuthenticationToken
                        request.getEmail(),//and then we provide to authenticationmanager and am will use the configured authentication provider
                        request.getPassword()// in thisuserdetailservice will find user and pwencoder verifies pw if credentical correct then auth object return
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();// getprincipal will give authenticateduser and cast to userdetail
        String token = jwtService.generateToken(userDetails);// now uathenticateduser will pass to jwtservice and jwtservice with usname and requiredclaim generate jwt
        return ResponseEntity.ok(new LoginResponse(token));// and generated token store in token variable and we returnjwt in response to client now jwt is come like -ehhyceg9,,
    }
}
