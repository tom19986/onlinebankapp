package com.onlinebanking.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateDTO {

       @NotBlank(message = "old password is mandatory")
       @Size(min = 6, message = "New password must be at least 6 characters long")
        String oldPassword;
        @NotBlank(message = "New password is mandatory")
        @Size(min = 6, message = "New password must be at least 6 characters long")
         String newPassword;

        @NotBlank(message = "Confirm password is mandatory")
         String confirmPassword;
    }



