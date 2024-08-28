package com.onlinebanking.user.repository;

import com.onlinebanking.user.model.Role;
import com.onlinebanking.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


   User findByUsername(String username);
    User findByPhoneNumber(String phoneNumber);
   User findByFirstName(String firstName);
    User findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<User> findByRole(Role role); // Filter by a single role (ADMIN or USER)

    Page<User> findByRoleIn(List<Role> roles,Pageable pageable); // Filter by multiple roles (e.g., ADMIN and USER)
    //-------------
    // Methods for partial matches
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    Page<User> findByEmailContainingIgnoreCase(String email,Pageable pageable);

    Page<User> findByPhoneNumberContainingIgnoreCase(String phoneNumber,Pageable pageable);


}

