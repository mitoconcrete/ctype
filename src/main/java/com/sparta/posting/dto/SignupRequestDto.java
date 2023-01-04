package com.sparta.posting.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {

    @Size(min=4,max=10)
    @Pattern(regexp = "[a-z0-9]+")
    private String username;
    @Size(min=8,max=15)
    @Pattern(regexp = "^[a-zA-Z0-9`~!@#$%^&*()-_=+\\|[]{};:'\",.<>/?]]+")
    private String password;
    private boolean admin;
    private String adminToken;
}
