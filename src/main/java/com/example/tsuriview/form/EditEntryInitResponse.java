package com.example.tsuriview.form;

import java.util.List;
import java.util.Map;

import com.example.tsuriview.entity.Fish;
import com.example.tsuriview.entity.Method;
import com.example.tsuriview.entity.Place;
import com.example.tsuriview.entity.Prefecture;

import lombok.Data;

@Data
public class EditEntryInitResponse {
	
	private List<Prefecture> prefectureList;
	
	private Map<String, List<Place>> placeListMap;
	
	private List<Fish> fishList;
	
	private List<Method> methodList;
	
	private EditEntryForm entry;

}
