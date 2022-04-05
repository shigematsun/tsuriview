package com.example.tsuriview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tsuriview.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
}
