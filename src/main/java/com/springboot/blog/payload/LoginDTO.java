package com.springboot.blog.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @NotEmpty(message = "Username or Email is required")
    private String usernameOrEmail;

    @NotEmpty(message = "Password is required")
    private String password;
}
