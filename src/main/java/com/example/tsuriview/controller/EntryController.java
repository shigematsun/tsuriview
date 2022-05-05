package com.example.tsuriview.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tsuriview.form.EditEntryForm;
import com.example.tsuriview.form.EditEntryInitResponse;
import com.example.tsuriview.form.SearchEntriesInitResponse;
import com.example.tsuriview.form.ShowEntriesRequest;
import com.example.tsuriview.form.ShowEntriesResponse;
import com.example.tsuriview.form.ShowEntryResponse;
import com.example.tsuriview.form.TopEntriesResponse;
import com.example.tsuriview.service.EditEntryService;
import com.example.tsuriview.service.ShowEntriesService;
import com.example.tsuriview.service.ShowEntryService;

@RestController
@RequestMapping("/api/entries")
public class EntryController {

	@Autowired
	EditEntryService editEntryService;

	@Autowired
	ShowEntryService showEntryService;

	@Autowired
	ShowEntriesService showEntriesService;

	@GetMapping(value = "/edit/init")
	public EditEntryInitResponse editEntryInit(@RequestParam("id") Optional<Integer> id) {
		return editEntryService.createInitResponse(id);
	}

	@Transactional
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String edit(@AuthenticationPrincipal UserDetails user, @RequestBody EditEntryForm request) {
		return editEntryService.editEntry(request, user.getUsername());
	}

	@GetMapping(value = "/{id}")
	public ShowEntryResponse showEntry(@AuthenticationPrincipal UserDetails user, @PathVariable("id") Integer id) {
		return showEntryService.createShowResponse(id,
				Optional.ofNullable(user).map(UserDetails::getUsername).orElse(null));
	}

	@GetMapping
	public ShowEntriesResponse showEntries(@ModelAttribute ShowEntriesRequest request) {
		return showEntriesService.createShowResponse(request);
	}

	@GetMapping(value = "/init")
	public SearchEntriesInitResponse searchEntriesInit() {
		return showEntriesService.createSearchInitResponse();
	}

	@GetMapping(value = "/top")
	public TopEntriesResponse topEntries(@RequestParam("userId") Optional<String> userId) {
		return showEntriesService.createTopResponse(userId);
	}
}
