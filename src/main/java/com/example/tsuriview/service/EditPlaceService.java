package com.example.tsuriview.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.tsuriview.entity.Place;
import com.example.tsuriview.form.EditPlaceForm;
import com.example.tsuriview.form.EditPlaceInitResponse;
import com.example.tsuriview.repository.PlaceRepository;
import com.example.tsuriview.repository.PrefectureRepository;

@Service
public class EditPlaceService {

	@Autowired
	PrefectureRepository prefectureRepository;
	@Autowired
	PlaceRepository placeRepository;

	public EditPlaceInitResponse createInitResponse() {
		EditPlaceInitResponse response = new EditPlaceInitResponse();
		response.setPrefectureList(prefectureRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
		List<Place> placeList = placeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
		// 場所を都道府県ごとのマップに変換
		Map<String, List<Place>> placeListMap = placeList.stream().collect(Collectors.groupingBy(Place::getPrefecture));
		response.setPlaceListMap(placeListMap);
		return response;
	}

	public EditPlaceForm createShowResponse(Integer id) {
		EditPlaceForm response = new EditPlaceForm();
		Place data = placeRepository.findById(id).get();
		response.setId(Optional.of(data.getId()));
		response.setName(data.getName());
		response.setPrefecture(data.getPrefecture());
		response.setTidePc(data.getTidePc());
		response.setTideHc(data.getTideHc());
		response.setMapUrl(data.getMapUrl());
		return response;
	}

	public String editPlace(EditPlaceForm request) {
		Place entity = request.getId().map(id -> placeRepository.findById(id).get()).orElse(new Place());
		entity.setName(request.getName());
		entity.setPrefecture(request.getPrefecture());
		entity.setTidePc(request.getTidePc());
		entity.setTideHc(request.getTideHc());
		entity.setMapUrl(request.getMapUrl());
		placeRepository.save(entity);
		return "成功しました";
	}
}
