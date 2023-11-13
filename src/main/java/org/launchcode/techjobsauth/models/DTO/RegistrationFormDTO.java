package org.launchcode.techjobsauth.models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegistrationFormDTO extends LoginFormDTO{
    @NotNull
    @NotBlank
    @Size(min = 3, max = 20, message = "password should be within 3-20 characters")
    private String verifyPassword;

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }
}
