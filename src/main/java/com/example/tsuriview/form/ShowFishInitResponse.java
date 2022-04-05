package com.example.tsuriview.form;

import java.util.List;

import com.example.tsuriview.entity.Fish;

import lombok.Data;

@Data
public class ShowFishInitResponse {

	private List<Fish> fishList;
}
