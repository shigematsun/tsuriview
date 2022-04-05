package com.example.tsuriview.form;

import java.util.List;
import java.util.Map;

import com.example.tsuriview.entity.Place;
import com.example.tsuriview.entity.Prefecture;

import lombok.Data;

@Data
public class ShowPlaceInitResponse {
	
	private List<Prefecture> prefectureList;
	
	private Map<String, List<Place>> placeListMap;

}
