package com.onlinebanking.user.dto;

import com.onlinebanking.user.util.ErrorMessageConstants;
import com.onlinebanking.user.util.SuccessMessageConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = ErrorMessageConstants.USERNAME_MANDATORY)
    private String username;
    @NotBlank(message = ErrorMessageConstants.PASSWORD_MANDATORY)
    private String password;
}
