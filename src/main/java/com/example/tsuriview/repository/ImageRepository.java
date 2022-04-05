package com.example.tsuriview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tsuriview.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
	
	List<Image> findByEntryId(Integer id);
	
	List<Image> findByEntryIdIsNull();
	
	List<Image> findByEntryIdAndEntryIndex(Integer entryId, Integer entryIndex);
	
	void deleteByEntryId(Integer entryId);
}
