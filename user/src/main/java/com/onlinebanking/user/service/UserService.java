package com.onlinebanking.user.service;

import com.onlinebanking.user.dto.*;
import com.onlinebanking.user.model.Role;
import com.onlinebanking.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public UserResponseDTO getUserByCriteria(Long id,String username, String email, String phoneNumber);
    public Page<UserResponseDTO> getUsersByCriteria(String username, String email, String phoneNumber, List<Role> roles, Pageable pageable);
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO);
    public Page<UserResponseDTO> getAllUsers(Pageable pageable);
    public String deleteUserById(Long id);
    public String loginUser(LoginRequestDTO loginRequestDTO);
    public String logoutUser(Long id);
    public String forgetPassword(Long id);
    public String updatePassword(Long id, PasswordUpdateDTO passwordUpdateDTO);
    public String changePassword(Long id, PasswordChangeDTO passwordChangeDTO);
    public String unblockUser(Long userId,Long adminId);


}