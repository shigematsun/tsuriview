package com.example.tsuriview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tsuriview.entity.Method;

@Repository
public interface MethodRepository extends JpaRepository<Method, Integer> {
}
