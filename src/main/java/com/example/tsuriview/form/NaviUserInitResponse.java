package com.example.tsuriview.form;

import java.util.List;

import lombok.Data;

@Data
public class NaviUserInitResponse {
	
	@Data
	public static class User {
		private String id;

		private String name;
	}
	
	private List<User> userList;

}
