package com.example.template.service;


import com.example.template.dto.request.LoginRequestDTO;
import com.example.template.dto.request.RegisterRequestDTO;
import com.example.template.dto.response.LoginResponseDTO;
import com.example.template.dto.response.RegisterResponseDTO;
import com.example.template.enums.Role;
import com.example.template.model.User;
import com.example.template.repository.UserRepository;
import io.jsonwebtoken.security.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {




    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;


    private final AuthenticationManager authenticationManager;



    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }






    public RegisterResponseDTO register(RegisterRequestDTO request) {
        try {
            User user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .role(Role.USER)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            userRepository.save(user);

            return RegisterResponseDTO.builder()
                    .message("Account Created")
                    .name(user.getName())
                    .email(request.getEmail())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("User already exists");
        }
    }


    public LoginResponseDTO login(LoginRequestDTO request){
        try{
            String email = request.getEmail();

            if(findByEmail(email) == null){
                throw new UsernameNotFoundException("Account does not exists");
            }
            User user = findByEmail(email);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));

            return LoginResponseDTO.builder().message("User logged in successfully").email(email).role(user.getRole().name()).build();
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid username or password");
        }
    }


}
