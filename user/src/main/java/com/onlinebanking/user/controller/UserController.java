package com.onlinebanking.user.controller;

import com.onlinebanking.user.dto.*;
import com.onlinebanking.user.model.Role;
import com.onlinebanking.user.model.User;
import com.onlinebanking.user.service.UserService;
import com.onlinebanking.user.util.SuccessMessageConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO response = userService.registerUser(userRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "username") String sortBy,
                                                             @RequestParam(defaultValue = "asc") String sortDir)
    {// Determine sort direction
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserResponseDTO> users = userService.getAllUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<UserResponseDTO> getUserByCriteria(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber)
           {


        // Call the service method to get the user based on criteria
        UserResponseDTO user = userService.getUserByCriteria(id,username, email, phoneNumber);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            // Return HTTP 404 if no exact match is found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        String response = userService.deleteUserById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        String response = userService.loginUser(loginRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout/{id}")
    public ResponseEntity<String> logoutUser(@PathVariable Long id) {
        String response = userService.logoutUser(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forget-password/{id}")
    public ResponseEntity<String> forgetPassword(@PathVariable Long id) {
        String response = userService.forgetPassword(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/change-password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable Long id,@Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
        String response = userService.changePassword(id, passwordChangeDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/update-password/{id}")
    public ResponseEntity<String> updatePassword(@PathVariable Long id,@Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        String response = userService.updatePassword(id, passwordUpdateDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/unblock")
    public ResponseEntity<String> unblockUser(@RequestParam Long userId,@RequestParam Long adminId) {
        String response = userService.unblockUser(userId,adminId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Combined method for fetching user(s) by username, email, or phone number with partial match
    @GetMapping("/searchList")
    public ResponseEntity<Page<UserResponseDTO>> getUsersByCriteria(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) List<Role> roles,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // Determine sort direction
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserResponseDTO> usersPage = userService.getUsersByCriteria(username, email, phoneNumber,roles, pageable);

        if (usersPage.hasContent()) {
            return new ResponseEntity<>(usersPage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
