package com.example.template.dto.response;


import com.example.template.enums.Role;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LoginResponseDTO {



    private String message;

    private String email;

    private String role;
}
