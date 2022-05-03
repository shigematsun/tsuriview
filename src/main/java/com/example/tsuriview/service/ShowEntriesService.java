package com.example.tsuriview.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.tsuriview.entity.Entry;
import com.example.tsuriview.entity.EntrySpecs;
import com.example.tsuriview.entity.Fish;
import com.example.tsuriview.entity.Image;
import com.example.tsuriview.entity.Place;
import com.example.tsuriview.form.SearchEntriesInitResponse;
import com.example.tsuriview.form.ShowEntriesEntryInfo;
import com.example.tsuriview.form.ShowEntriesRequest;
import com.example.tsuriview.form.ShowEntriesResponse;
import com.example.tsuriview.form.TopEntriesResponse;
import com.example.tsuriview.repository.EntryRepository;
import com.example.tsuriview.repository.FishRepository;
import com.example.tsuriview.repository.ImageRepository;
import com.example.tsuriview.repository.MethodRepository;
import com.example.tsuriview.repository.PlaceRepository;
import com.example.tsuriview.repository.PrefectureRepository;
import com.example.tsuriview.repository.UserRepository;
import com.example.tsuriview.util.ImageUtils;

@Service
public class ShowEntriesService {

	@Autowired
	EntryRepository entryRepository;
	@Autowired
	ImageRepository imageRepository;
	@Autowired
	PrefectureRepository prefectureRepository;
	@Autowired
	PlaceRepository placeRepository;
	@Autowired
	FishRepository fishRepository;
	@Autowired
	MethodRepository methodRepository;
	@Autowired
	UserRepository userRepository;

	@Autowired
	ImageUtils imageUtils;

	@Value("${entry.page.size}")
	private Integer pageSize;

	@Value("${top.entry.size}")
	private Integer topSize;

	private static final String DATE_FORMAT = "yyyy年MM月dd日";

	public SearchEntriesInitResponse createSearchInitResponse() {
		SearchEntriesInitResponse response = new SearchEntriesInitResponse();
		response.setPrefectureList(prefectureRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
		List<Place> placeList = placeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
		// 場所を都道府県ごとのマップに変換
		Map<String, List<Place>> placeListMap = placeList.stream().collect(Collectors.groupingBy(Place::getPrefecture));
		response.setPlaceListMap(placeListMap);
		response.setFishList(fishRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
		response.setMethodList(methodRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));

		return response;
	}

	public ShowEntriesResponse createShowResponse(ShowEntriesRequest request) {
		ShowEntriesResponse response = new ShowEntriesResponse();
		// @formatter:off
		Specification<Entry> spec = Specification.where(
				EntrySpecs.dateAfter(Optional.ofNullable(request.getFromDate())))
				.and(EntrySpecs.dateBefore(Optional.ofNullable(request.getToDate())))
				.and(EntrySpecs.monthEquals(Optional.ofNullable(request.getMonth())))
				.and(EntrySpecs.prefectureEquals(Optional.ofNullable(request.getPrefecture())))
				.and(EntrySpecs.placeEquals(Optional.ofNullable(request.getPlace())))
				.and(EntrySpecs.existsFish(Optional.ofNullable(request.getFish())))
				.and(EntrySpecs.existsMethod(Optional.ofNullable(request.getMethod())));
		Page<Entry> entryList = entryRepository.findAll(spec, 
				PageRequest.of(
						Optional.ofNullable(request.getPage()).orElse(1) - 1, 
						pageSize,
						Sort.by("date").descending().and(Sort.by("startTime").descending())));
		// @formatter:on

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		response.setEntryList(entryList.stream().map(entry -> {
			ShowEntriesEntryInfo entryInfo = new ShowEntriesEntryInfo();

			List<Image> image = imageRepository.findByEntryIdAndEntryIndex(entry.getId(), 1);
			entryInfo.setImageUrl(image.isEmpty() ? "" : imageUtils.getUrlByKey(image.get(0).getKey()));
			entryInfo.setId(entry.getId());
			entryInfo.setUserName(userRepository.findById(entry.getUserId()).get().getDisplayName());
			entryInfo.setDate(sdf.format(entry.getDate()));
			entryInfo.setPlace(placeRepository.findById(entry.getPlace()).map(Place::getName).orElse(""));
			entryInfo.setFishList(entry.getFishList().stream().map(fish -> fish.getFish())
					.map(fishId -> fishRepository.findById(fishId)).map(opt -> opt.map(Fish::getName).orElse(""))
					.distinct().collect(Collectors.toList()));

			return entryInfo;
		}).collect(Collectors.toList()));

		response.setTotalPages(entryList.getTotalPages());

		return response;
	}

	public TopEntriesResponse createTopResponse() {
		TopEntriesResponse response = new TopEntriesResponse();
		Page<Entry> entryList = entryRepository.findAll(
				PageRequest.of(0, topSize, Sort.by("date").descending().and(Sort.by("startTime").descending())));

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		response.setEntryList(entryList.stream().map(entry -> {
			ShowEntriesEntryInfo entryInfo = new ShowEntriesEntryInfo();

			List<Image> image = imageRepository.findByEntryIdAndEntryIndex(entry.getId(), 1);
			entryInfo.setImageUrl(image.isEmpty() ? "" : imageUtils.getUrlByKey(image.get(0).getKey()));
			entryInfo.setId(entry.getId());
			entryInfo.setUserName(userRepository.findById(entry.getUserId()).get().getDisplayName());
			entryInfo.setDate(sdf.format(entry.getDate()));
			entryInfo.setPlace(placeRepository.findById(entry.getPlace()).map(Place::getName).orElse(""));
			entryInfo.setFishList(entry.getFishList().stream().map(fish -> fish.getFish())
					.map(fishId -> fishRepository.findById(fishId)).map(opt -> opt.map(Fish::getName).orElse(""))
					.distinct().collect(Collectors.toList()));

			return entryInfo;
		}).collect(Collectors.toList()));


		return response;
	}
}
