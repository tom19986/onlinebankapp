package com.onlinebanking.user.dto;

import com.onlinebanking.user.model.Gender;
import com.onlinebanking.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private String message;
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Role role;
    private Gender gender;
    private boolean isBlock=false;
    private LocalDateTime registerDate;
    private LocalDateTime lastLoginDate;
    private boolean isLogin=false;
    private LocalDateTime lastUpdatedDate;

}

