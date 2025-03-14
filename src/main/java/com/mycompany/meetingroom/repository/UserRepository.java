package com.mycompany.meetingroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.meetingroom.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Additional query methods if needed
}

