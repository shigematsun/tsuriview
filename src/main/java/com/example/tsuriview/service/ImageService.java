package com.example.tsuriview.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tsuriview.entity.Image;
import com.example.tsuriview.form.ImageInfo;
import com.example.tsuriview.repository.ImageRepository;
import com.example.tsuriview.util.ImageUtils;

@Service
public class ImageService {

	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	ImageUtils imageUtils;

	public void regist(String key) {
		Image image = new Image();
		image.setKey(key);
		imageRepository.save(image);
	}

	public List<ImageInfo> getEnableList(Optional<Integer> entryId) {
		List<Image> unuseList = imageRepository.findByEntryIdIsNull();
		// 詳細編集時の考慮
		List<Image> ownList = entryId.isPresent() ? imageRepository.findByEntryId(entryId.get()) : Collections.emptyList();
		List<ImageInfo> imageInfoList = Stream.concat(ownList.stream(), unuseList.stream()).map(entity -> {
			ImageInfo info = new ImageInfo();
			info.setId(entity.getId());
			info.setUrl(imageUtils.getUrlByKey(entity.getKey()));
			return info;
		}).collect(Collectors.toList());
		
		return imageInfoList;
	}

}
