package com.example.tsuriview.form;

import java.util.List;

import com.example.tsuriview.entity.Fish;

import lombok.Data;

@Data
public class EditFishInitResponse {
	
	private List<Fish> fishList;
	
	private String imageBaseUrl;

}
