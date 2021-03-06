package com.example.tsuriview.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.tsuriview.form.ImageInfo;
import com.example.tsuriview.form.ResponseMessage;
import com.example.tsuriview.service.AmazonS3Service;
import com.example.tsuriview.service.ImageService;

@Controller
@RequestMapping("/api/images")
public class ImageController {

	@Autowired
	AmazonS3Service storageService;
	@Autowired
	ImageService imageService;

	@PostMapping()
	public ResponseEntity<ResponseMessage> uploadFile(@AuthenticationPrincipal UserDetails user,
			@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
		String message = "";
		try {
			String key = storageService.putObject(file.getInputStream(), name, file.getContentType(), file.getSize());

			imageService.regist(key, user.getUsername());
			message = "ファイルのアップロードに成功しました。: " + name;
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "ファイルをアップロードできませんでした。: " + name;
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	@GetMapping()
	@ResponseBody
	public List<ImageInfo> getListFiles(@AuthenticationPrincipal UserDetails user,
			@RequestParam("entryId") Optional<Integer> entryId) {
		return imageService.getEnableList(entryId, user.getUsername());
	}
}
