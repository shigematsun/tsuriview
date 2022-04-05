package com.example.tsuriview.form;

import java.util.Optional;

import lombok.Data;

@Data
public class EditFishForm {
	
	private Optional<Integer> id;
	
	private String name;
	
	private String imageKey;

}
