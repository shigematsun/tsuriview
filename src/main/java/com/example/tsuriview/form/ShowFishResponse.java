package com.example.tsuriview.form;

import java.util.List;

import lombok.Data;

@Data
public class ShowFishResponse {
	
	private Integer fishId;
	
	private String name;
	
	private String imageUrl;
	
	private Integer total;
	
	private Integer min;
	
	private Integer max;
	
	private List<Integer> monthCountList;
	
	private List<Integer> timeCountList;

}
