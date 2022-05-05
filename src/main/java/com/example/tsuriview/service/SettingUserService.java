package com.example.tsuriview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.tsuriview.entity.User;
import com.example.tsuriview.form.SettingUserForm;
import com.example.tsuriview.form.SettingUserInitResponse;
import com.example.tsuriview.repository.UserRepository;

@Service
public class SettingUserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public SettingUserInitResponse createInitResponse(UserDetails user) {
		SettingUserInitResponse response = new SettingUserInitResponse();
		response.setName(userRepository.findById(user.getUsername()).get().getDisplayName());
		return response;
	}

	public String update(UserDetails user, SettingUserForm request) {
		User entity = userRepository.findById(user.getUsername()).get();
		entity.setDisplayName(request.getName());
		if (StringUtils.hasLength(request.getPassword())) {
			entity.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		userRepository.save(entity);

		return "成功しました";
	}
}
