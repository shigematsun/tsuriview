package com.example.tsuriview.form;

import java.util.List;

import lombok.Data;

@Data
public class ShowEntriesEntryInfo {
	
	private Integer id;
	
	private String userName;
	
	private String imageUrl;
	
	private String date;
	
	private String place;
	
	private List<String> fishList;
}
