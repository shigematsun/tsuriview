package com.example.tsuriview.form;

import java.util.Optional;

import lombok.Data;

@Data
public class EditPlaceForm {
	
	private Optional<Integer> id;
	
	private String name;
	
	private String prefecture;
	
	private String tidePc;
	
	private String tideHc;
	
	private String mapUrl;

}
