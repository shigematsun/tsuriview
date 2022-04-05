package com.example.tsuriview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tsuriview.form.EditMethodForm;
import com.example.tsuriview.form.EditMethodInitResponse;
import com.example.tsuriview.service.EditMethodService;

@RestController
@RequestMapping("/api/methods")
public class MethodController {

	@Autowired
	EditMethodService editMethodService;

	@GetMapping(value = "/edit/init")
	public EditMethodInitResponse editEntryInit() {
		return editMethodService.createInitResponse();
	}

	@GetMapping(value = "/edit/{id}")
	public EditMethodForm editMethodShow(@PathVariable("id") Integer id) {
		return editMethodService.createShowResponse(id);
	}
	
	@Transactional
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String edit(@RequestBody EditMethodForm request) {
		return editMethodService.editMethod(request);
	}
}
