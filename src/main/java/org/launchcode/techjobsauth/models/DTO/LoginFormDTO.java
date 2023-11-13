package org.launchcode.techjobsauth.models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginFormDTO {
    @NotNull
    @NotBlank
    @Size(min = 3, max = 20, message = "username should be within 3-20 characters")
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 20, message = "password should be within 6-20 characters")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
