package com.example.tsuriview.form;

import java.util.List;

import lombok.Data;

@Data
public class ShowPlaceFishInfo {
	private String fishName;
	
	private Integer count;
	
	private Integer min;
	
	private Integer max;
	
	private List<String> methodList;
}
