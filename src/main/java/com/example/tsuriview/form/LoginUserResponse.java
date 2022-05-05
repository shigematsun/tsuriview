package com.example.tsuriview.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginUserResponse {
	
	private String name;
	
	@JsonProperty("isAdmin")
	private boolean isAdmin;

}
