package com.servicedesk.service_desk.repositories;

import com.servicedesk.service_desk.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    boolean existsByUsername(String username);

    Optional<UserModel> findByUsername(String username);
}
