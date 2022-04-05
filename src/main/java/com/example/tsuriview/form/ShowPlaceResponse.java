package com.example.tsuriview.form;

import java.util.List;

import lombok.Data;

@Data
public class ShowPlaceResponse {
	
	private String prefectureId;
	
	private Integer placeId;
	
	private String name;
	
	private Integer times;
	
	private List<ShowPlaceFishInfo> fishList;
	
	private List<List<String>> monthFishList;
	
	private String mapUrl;
	
	private String tideBaseUrl;
	
	private List<Integer> monthCountList;

}
