package com.example.tsuriview.form;

import lombok.Data;

@Data
public class ShowFishInfo {
	private String fishName;
	
	private Integer count;
	
	private Integer min;
	
	private Integer max;
	
	private String methodName;
	
	private String time;
}
