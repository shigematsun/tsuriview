package com.example.tsuriview.controller;

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

import com.example.tsuriview.form.EditPlaceForm;
import com.example.tsuriview.form.EditPlaceInitResponse;
import com.example.tsuriview.form.ShowPlaceInitResponse;
import com.example.tsuriview.form.ShowPlaceResponse;
import com.example.tsuriview.form.TopPlaceResponse;
import com.example.tsuriview.service.EditPlaceService;
import com.example.tsuriview.service.ShowPlaceService;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

	@Autowired
	ShowPlaceService showPlaceService;

	@Autowired
	EditPlaceService editPlaceService;

	@GetMapping(value = "/init")
	public ShowPlaceInitResponse showPlaceInit() {
		return showPlaceService.createInitResponse();
	}

	@GetMapping(value = "/{id}")
	public ShowPlaceResponse showPlace(@PathVariable("id") Integer id) {
		return showPlaceService.createShowResponse(id);
	}

	@GetMapping(value = "/top")
	public TopPlaceResponse topPlace(@RequestParam("month") Integer month) {
		return showPlaceService.createTopResponse(month);
	}

	@GetMapping(value = "/edit/init")
	public EditPlaceInitResponse editEntryInit() {
		return editPlaceService.createInitResponse();
	}

	@GetMapping(value = "/edit/{id}")
	public EditPlaceForm editPlaceShow(@PathVariable("id") Integer id) {
		return editPlaceService.createShowResponse(id);
	}

	@Transactional
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String edit(@RequestBody EditPlaceForm request) {
		return editPlaceService.editPlace(request);
	}
}
