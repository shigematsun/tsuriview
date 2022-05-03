package com.example.tsuriview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tsuriview.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
