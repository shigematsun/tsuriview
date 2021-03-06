package com.example.tsuriview.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tsuriview.form.EditFishForm;
import com.example.tsuriview.form.EditFishInitResponse;
import com.example.tsuriview.form.FishListResponse;
import com.example.tsuriview.form.ShowFishInitResponse;
import com.example.tsuriview.form.ShowFishResponse;
import com.example.tsuriview.form.TopFishResponse;
import com.example.tsuriview.service.EditFishService;
import com.example.tsuriview.service.ShowFishService;

@RestController
@RequestMapping("/api/fishes")
public class FishController {

	@Autowired
	ShowFishService showFishService;

	@Autowired
	EditFishService editFishService;

	@GetMapping(value = "/init")
	public ShowFishInitResponse showFishInit(@RequestParam("userId") Optional<String> userId) {
		return showFishService.createInitResponse(userId);
	}

	@GetMapping(value = "/{id}")
	public ShowFishResponse showFish(@PathVariable("id") Integer id, @RequestParam("userId") Optional<String> userId) {
		return showFishService.createShowResponse(id, userId);
	}

	@GetMapping(value = "/top")
	public TopFishResponse topFish(@RequestParam("month") Integer month,
			@RequestParam("userId") Optional<String> userId) {
		return showFishService.createTopResponse(month, userId);
	}

	@GetMapping()
	public FishListResponse showFishList(@RequestParam("userId") Optional<String> userId) {
		return showFishService.createFishListResponse(userId);
	}

	@GetMapping(value = "/edit/init")
	public EditFishInitResponse editFishInit() {
		return editFishService.createInitResponse();
	}

	@GetMapping(value = "/edit/{id}")
	public EditFishForm editFishShow(@PathVariable("id") Integer id) {
		return editFishService.createShowResponse(id);
	}

	@Transactional
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String edit(@RequestBody EditFishForm request) {
		return editFishService.editFish(request);
	}
}
