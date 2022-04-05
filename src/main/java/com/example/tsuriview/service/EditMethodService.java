package com.example.tsuriview.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.tsuriview.entity.Method;
import com.example.tsuriview.form.EditMethodForm;
import com.example.tsuriview.form.EditMethodInitResponse;
import com.example.tsuriview.repository.MethodRepository;

@Service
public class EditMethodService {

	@Autowired
	MethodRepository methodRepository;

	public EditMethodInitResponse createInitResponse() {
		EditMethodInitResponse response = new EditMethodInitResponse();
		response.setMethodList(methodRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
		return response;
	}

	public EditMethodForm createShowResponse(Integer id) {
		EditMethodForm response = new EditMethodForm();
		Method data = methodRepository.findById(id).get();
		response.setId(Optional.of(data.getId()));
		response.setName(data.getName());
		return response;
	}

	public String editMethod(EditMethodForm request) {
		Method entity = request.getId().map(id -> methodRepository.findById(id).get()).orElse(new Method());
		entity.setName(request.getName());
		methodRepository.save(entity);
		return "成功しました";
	}
}
