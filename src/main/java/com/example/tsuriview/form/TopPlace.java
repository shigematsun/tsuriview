package com.example.tsuriview.form;

import java.util.List;

import lombok.Data;

@Data
public class TopPlace {
	
	private Integer placeId;
	
	private String name;
	
	private String prefecture;
	
	private List<String> fishNameList;
	
	private Integer count;

}
