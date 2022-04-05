package com.example.tsuriview.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.tsuriview.entity.Fish;
import com.example.tsuriview.form.EditFishForm;
import com.example.tsuriview.form.EditFishInitResponse;
import com.example.tsuriview.repository.FishRepository;
import com.example.tsuriview.util.ImageUtils;

@Service
public class EditFishService {

	@Autowired
	FishRepository fishRepository;
	
	@Autowired
	ImageUtils imageUtils;

	public EditFishInitResponse createInitResponse() {
		EditFishInitResponse response = new EditFishInitResponse();
		response.setFishList(fishRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
		response.setImageBaseUrl(imageUtils.getBaseUrl());
		return response;
	}

	public EditFishForm createShowResponse(Integer id) {
		EditFishForm response = new EditFishForm();
		Fish data = fishRepository.findById(id).get();
		response.setId(Optional.of(data.getId()));
		response.setName(data.getName());
		response.setImageKey(data.getImageKey());
		return response;
	}

	public String editFish(EditFishForm request) {
		Fish entity = request.getId().map(id -> fishRepository.findById(id).get()).orElse(new Fish());
		entity.setName(request.getName());
		entity.setImageKey(request.getImageKey());
		fishRepository.save(entity);
		return "成功しました";
	}
}
