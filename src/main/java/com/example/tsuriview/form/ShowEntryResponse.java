package com.example.tsuriview.form;

import java.util.List;

import lombok.Data;

@Data
public class ShowEntryResponse {
	
	private List<String> imageUrlList;
	
	private String date;
	
	private String startTime;
	
	private String endTime;
	
	private Integer totalFish;
	
	private List<String> fishNameList;
	
	private String prefecture;
	
	private String place;
	
	private String mapUrl;
	
	private List<ShowFishInfo> fishList;
	
	private String memo;
	
	private String tideUrl;
	
	private Integer nextId;
	
	private Integer prevId;

}
