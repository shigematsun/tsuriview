package com.example.tsuriview.form;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;


import lombok.Data;

@Data
public class EditEntryForm {
	
	private Optional<Integer> id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	private String startTime;
	
	private String endTime;
	
	private String prefecture;
	
	private Integer place;
	
	private List<FishInfo> fishInfoList;
	
	private String memo;
	
	private List<ImageInfo> imageList;

}
