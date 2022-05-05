package com.example.tsuriview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tsuriview.entity.EntryFish;
import com.example.tsuriview.entity.EntryFishKey;

@Repository
public interface EntryFishRepository extends JpaRepository<EntryFish, EntryFishKey> {
	
	List<EntryFish> findByFish(Integer fishId);
	
	List<EntryFish> findByFishAndUserId(Integer fishId, String userId);
	
	void deleteByEntryId(Integer entryId);
}
