package com.example.template.controller;


import com.example.template.dto.request.LoginRequestDTO;
import com.example.template.dto.request.RegisterRequestDTO;
import com.example.template.dto.response.LoginResponseDTO;
import com.example.template.service.AuthService;
import com.example.template.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    private final JwtService jwtService;




    @GetMapping("/greet")
    public ResponseEntity<?> greet(){
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request){
        try{
            return ResponseEntity.ok(authService.register(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("messages",e.getMessage()));
        }
    }





    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO requestDTO, HttpServletResponse response){
        try{
            LoginResponseDTO loginResponseDTO = authService.login(requestDTO);
            String accessToken = jwtService.generateToken(requestDTO.getEmail());

            ResponseCookie cookie = ResponseCookie.from("accessToken",accessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(600)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE,cookie.toString());
            return ResponseEntity.ok(loginResponseDTO);
        }catch (BadCredentialsException e){
            return new ResponseEntity<>(Map.of("messages",e.getMessage()), HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("messages",e.getMessage()));
        }
    }
}
