package com.example.tsuriview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.tsuriview.form.LoginUserResponse;
import com.example.tsuriview.repository.UserRepository;

@Service
public class LoginUserService {

	@Autowired
	UserRepository userRepository;

	public LoginUserResponse createLoginUserResponse(UserDetails user) {
		LoginUserResponse response = new LoginUserResponse();
		response.setName(userRepository.findById(user.getUsername()).get().getDisplayName());
		response.setAdmin(user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("admin")));
		return response;
	}
}
