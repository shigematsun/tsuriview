package com.example.tsuriview.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.tsuriview.entity.Entry;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Integer>, JpaSpecificationExecutor<Entry> {
	
	List<Entry> findByPlace(Integer place);
	
	List<Entry> findByMonth(Integer month);
	
	List<Entry> findByDateGreaterThanOrderByDateAscStartTimeAsc(Date date);
}
