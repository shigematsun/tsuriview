package com.example.tsuriview.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.tsuriview.entity.User;
import com.example.tsuriview.form.NaviUserInitResponse;
import com.example.tsuriview.repository.UserRepository;

@Service
public class NaviUserService {

	@Autowired
	UserRepository userRepository;

	public NaviUserInitResponse createInitResponse() {
		NaviUserInitResponse response = new NaviUserInitResponse();
		List<User> userDataList = userRepository.findAll(Sort.by(Sort.Direction.ASC, "displayName"));
		response.setUserList(userDataList.stream().map(data -> {
			NaviUserInitResponse.User user = new NaviUserInitResponse.User();
			user.setId(data.getId());
			user.setName(data.getDisplayName());
			return user;
		}).collect(Collectors.toList()));
		return response;
	}
}
