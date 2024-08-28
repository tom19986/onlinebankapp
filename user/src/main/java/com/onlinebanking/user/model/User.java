package com.onlinebanking.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private int invalidAttemptCount;

}
