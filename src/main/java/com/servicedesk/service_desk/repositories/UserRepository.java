package com.servicedesk.service_desk.repositories;

import com.servicedesk.service_desk.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    boolean existsByUsername(String username);

    UserDetails findByUsername(String username);

    @Query("SELECT u FROM UserModel u WHERE u.username = :username")
    public Optional<UserModel> findByUsernameOpt(@Param("username") String username);

}
