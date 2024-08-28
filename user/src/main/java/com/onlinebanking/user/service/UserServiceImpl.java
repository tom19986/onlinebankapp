package com.onlinebanking.user.service;

import com.onlinebanking.user.dto.*;
import com.onlinebanking.user.exception.*;
import com.onlinebanking.user.model.Role;
import com.onlinebanking.user.model.User;
import com.onlinebanking.user.repository.UserRepository;
import com.onlinebanking.user.util.ErrorMessageConstants;
import com.onlinebanking.user.util.SuccessMessageConstants;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    
    //----private----------
    private User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessageConstants.USER_NOT_FOUND));
    }
    //------
    private User getByEmail(String email) {
        User user= userRepository.findByEmail(email);
        if (user == null)
        {throw new UserNotFoundException(ErrorMessageConstants.USER_NOT_FOUND);}
         return user;}

    //------
    private User getByPhoneNumber(String phoneNumber) {
        User user= userRepository.findByPhoneNumber(phoneNumber);
        if (user == null)
        {throw new UserNotFoundException(ErrorMessageConstants.USER_NOT_FOUND);}
        return user;}
    //------
    //------
    private User getByUsername(String username) {
        User user= userRepository.findByUsername(username);
        if (user == null)
        {throw new UserNotFoundException(ErrorMessageConstants.USER_NOT_FOUND);}
        return user;}
    //------
    //--search--------
    @Override
    public UserResponseDTO getUserByCriteria(Long id,String username, String email, String phoneNumber) {
        User user = null;
         if (id != null){user=getById(id);}
        else if (username != null) {user = getByUsername(username);}
        else if (email != null) {user = getByEmail(email);}
        else if (phoneNumber != null) {user = getByPhoneNumber(phoneNumber);}
        return modelMapper.map(user, UserResponseDTO.class);}
    // Partial search with pagination and sorting
    @Override
    public Page<UserResponseDTO> getUsersByCriteria(String username, String email, String phoneNumber, List<Role> roles, Pageable pageable) {
        Page<User> usersPage;

        if (username != null && !username.isEmpty()) {
            usersPage = userRepository.findByUsernameContainingIgnoreCase(username, pageable);
        } else if (email != null && !email.isEmpty()) {
            usersPage = userRepository.findByEmailContainingIgnoreCase(email, pageable);
        } else if (phoneNumber != null && !phoneNumber.isEmpty()) {
            usersPage = userRepository.findByPhoneNumberContainingIgnoreCase(phoneNumber, pageable);
        } else if (roles != null && !roles.isEmpty()) {
            usersPage = userRepository.findByRoleIn(roles, pageable);
        } else {
            usersPage = Page.empty(pageable); // Return an empty page if no criteria are provided
        }

        return usersPage.map(user -> modelMapper.map(user, UserResponseDTO.class));
    }

    //--------


    @Override
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        // Check if the user already exists by username or email
        if (userRepository.existsByUsername(userRequestDTO.getUsername()) ||
                userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException(ErrorMessageConstants.USER_ALREADY_REGISTERED);
        }


        User newUser = modelMapper.map(userRequestDTO, User.class);

        // Set additional fields that are not covered by the mapping
        newUser.setRegisterDate(LocalDateTime.now());
        newUser.setBlock(false); // Assuming new users are not blocked by default
        newUser.setLogin(false); // New users are not logged in by default

        // Save the new user to the repository
        User savedUser = userRepository.save(newUser);

        // Convert the saved User entity to UserResponseDTO using ModelMapper
        UserResponseDTO userResponseDTO= modelMapper.map(savedUser, UserResponseDTO.class);
        userResponseDTO.setMessage(SuccessMessageConstants.USER_REGISTERED_SUCCESS);
        return userResponseDTO;
    }

//---get------
    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        // Fetch paginated and sorted users from the repository
        Page<User> usersPage = userRepository.findAll(pageable);

        // Convert each User entity to a UserResponseDTO using ModelMapper
        return usersPage.map(user -> modelMapper.map(user, UserResponseDTO.class));
    }

    @Override
    public String deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessageConstants.USER_NOT_FOUND));
        userRepository.delete(user);
        return SuccessMessageConstants.USER_DELETED_SUCCESS;
    }

    @Override
    public String loginUser(LoginRequestDTO loginRequestDTO) {
        // Fetch user from repository
        User user = getByUsername(loginRequestDTO.getUsername());
        // Check if the user is blocked
        if (user.isBlock()) {
            throw new AccountBlockedException(ErrorMessageConstants.ACCOUNT_BLOCKED);
        }

        // Check if the password is correct
        if (!user.getPassword().equals(loginRequestDTO.getPassword())) {
            // Increment wrong attempt counter
            user.setInvalidAttemptCount(user.getInvalidAttemptCount() + 1);

            // Block user if wrong attempts exceed limit
            if (user.getInvalidAttemptCount() >= 3) {
                user.setBlock(true);
            }
            userRepository.save(user); // Save user with updated status
            throw new PasswordMismatchException(ErrorMessageConstants.PASSWORD_INCORRECT);
        }

        // Reset wrong attempt counter on successful login
        user.setInvalidAttemptCount(0);
        user.setLogin(true);
        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user); // Save updated user

        return SuccessMessageConstants.LOGIN_SUCCESS;
    }


    @Override
    public String logoutUser(Long id) {
        User user = getById(id);
        if (user.isBlock()){throw new AccountBlockedException(ErrorMessageConstants.ALREADY_ACCOUNT_BLOCKED);}

        if (user.isLogin()) {
            user.setLogin(false);
            userRepository.save(user); // Ensure to save the updated user
        }else throw new UserNotLoggedInException(ErrorMessageConstants.USER_ALREADY_LOGGEDOUT);
        return SuccessMessageConstants.LOGOUT_SUCCESS;
    }

    @Override
    public String forgetPassword(Long id) {
        User user = getById(id);
        return SuccessMessageConstants.PASSWORD_RESET_URL + user.getId();
    }

    @Override
    public String updatePassword(Long id, PasswordUpdateDTO passwordUpdateDTO) {
        User user = getById(id);

        if (passwordUpdateDTO.getNewPassword().equals(passwordUpdateDTO.getConfirmPassword())) {
            throw new PasswordMismatchException(ErrorMessageConstants.PASSWORD_INCORRECT);
        }
        if (!user.getPassword().equals(passwordUpdateDTO.getOldPassword())) {
            throw new PasswordMismatchException(ErrorMessageConstants.CURRENT_PASSWORD_INCORRECT);
        }
        user.setPassword(passwordUpdateDTO.getNewPassword());
        userRepository.save(user); // Ensure to save the updated user
        return SuccessMessageConstants.PASSWORD_UPDATED_SUCCESS;
    }

    @Override
    public String changePassword(Long id, PasswordChangeDTO passwordChangeDTO) {
        User user = getById(id);

        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmPassword())) {
            throw new PasswordMismatchException(ErrorMessageConstants.PASSWORD_INCORRECT);
        }
        user.setPassword(passwordChangeDTO.getNewPassword());
        userRepository.save(user); // Ensure to save the updated user
        return SuccessMessageConstants.PASSWORD_UPDATED_SUCCESS;
    }


    //-------------
    @Override
    public String unblockUser(Long userId,Long adminId) {
       User user = getById(adminId);
        if (!(user == null) && (user.isLogin())) {
            User u=getById(userId);
            u.setBlock(false);
            u.setInvalidAttemptCount(0);
            userRepository.save(u);
        }else throw new UserNotFoundException(ErrorMessageConstants.USER_NOT_FOUND);
        return SuccessMessageConstants.UNBLOCK_SUCCESS;
    }
    //----------



}
