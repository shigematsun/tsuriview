package com.example.tsuriview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tsuriview.entity.Prefecture;

@Repository
public interface PrefectureRepository extends JpaRepository<Prefecture, String> {
}
