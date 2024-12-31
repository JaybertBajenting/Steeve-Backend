




package com.example.template.controller;
import com.example.template.model.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authenticated")
public class AuthenticatedController {



    @GetMapping("/me")
    public ResponseEntity<?> getMe(){
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user == null){
                throw new EntityNotFoundException("Account not found");
            }
            return ResponseEntity.ok(user);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("messages",e.getMessage()));
        }
    }




    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response){
        try{
            ResponseCookie cookie = ResponseCookie.from("accessToken",null)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(0)
                    .build();

            SecurityContextHolder.getContext().setAuthentication(null);
            SecurityContextHolder.clearContext();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("messages",e.getMessage()));
        }
    }
}
