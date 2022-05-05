package com.example.tsuriview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tsuriview.form.LoginUserResponse;
import com.example.tsuriview.form.NaviUserInitResponse;
import com.example.tsuriview.form.SettingUserForm;
import com.example.tsuriview.form.SettingUserInitResponse;
import com.example.tsuriview.service.LoginUserService;
import com.example.tsuriview.service.NaviUserService;
import com.example.tsuriview.service.SettingUserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	NaviUserService naviUserService;
	@Autowired
	LoginUserService loginUserService;
	@Autowired
	SettingUserService settingUserService;

	@GetMapping()
	public NaviUserInitResponse getUserList() {
		return naviUserService.createInitResponse();
	}

	@GetMapping("/login")
	public LoginUserResponse getLoginUser(@AuthenticationPrincipal UserDetails user) {
		return loginUserService.createLoginUserResponse(user);
	}

	@GetMapping("/setting")
	public SettingUserInitResponse getSettingUserInit(@AuthenticationPrincipal UserDetails user) {
		return settingUserService.createInitResponse(user);
	}

	@PostMapping(value = "/setting", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String updateSettingUser(@AuthenticationPrincipal UserDetails user, @RequestBody SettingUserForm request) {
		return settingUserService.update(user, request);
	}
}
