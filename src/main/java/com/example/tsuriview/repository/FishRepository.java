package com.example.tsuriview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.tsuriview.entity.Fish;

@Repository
public interface FishRepository extends JpaRepository<Fish, Integer>, JpaSpecificationExecutor<Fish> {
}
