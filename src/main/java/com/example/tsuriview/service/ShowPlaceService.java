package com.example.tsuriview.service;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.tsuriview.entity.Entry;
import com.example.tsuriview.entity.EntryFish;
import com.example.tsuriview.entity.EntrySpecs;
import com.example.tsuriview.entity.Fish;
import com.example.tsuriview.entity.Method;
import com.example.tsuriview.entity.Place;
import com.example.tsuriview.form.ShowPlaceFishInfo;
import com.example.tsuriview.form.ShowPlaceInitResponse;
import com.example.tsuriview.form.ShowPlaceResponse;
import com.example.tsuriview.form.TopPlace;
import com.example.tsuriview.form.TopPlaceResponse;
import com.example.tsuriview.repository.EntryFishRepository;
import com.example.tsuriview.repository.EntryRepository;
import com.example.tsuriview.repository.FishRepository;
import com.example.tsuriview.repository.MethodRepository;
import com.example.tsuriview.repository.PlaceRepository;
import com.example.tsuriview.repository.PrefectureRepository;

@Service
public class ShowPlaceService {

	@Autowired
	PrefectureRepository prefectureRepository;
	@Autowired
	PlaceRepository placeRepository;
	@Autowired
	EntryRepository entryRepository;
	@Autowired
	EntryFishRepository entryFishRepository;
	@Autowired
	FishRepository fishRepository;
	@Autowired
	MethodRepository methodRepository;

	@Value("${tide.url}")
	private String TIDE_URL;

	@Value("${top.place.size}")
	private Integer TOP_PLACE_SIZE;

	public ShowPlaceInitResponse createInitResponse() {
		ShowPlaceInitResponse response = new ShowPlaceInitResponse();
		response.setPrefectureList(prefectureRepository.findAll());
		List<Place> placeList = placeRepository.findAll();
		// 場所を都道府県ごとのマップに変換
		Map<String, List<Place>> placeListMap = placeList.stream().collect(Collectors.groupingBy(Place::getPrefecture));
		response.setPlaceListMap(placeListMap);

		return response;
	}

	public ShowPlaceResponse createShowResponse(Integer id) {
		ShowPlaceResponse response = new ShowPlaceResponse();
		response.setPlaceId(id);
		Place place = placeRepository.findById(id).get();
		response.setPrefectureId(place.getPrefecture());
		response.setName(place.getName());
		response.setMapUrl(place.getMapUrl());

		List<Entry> entryList = entryRepository.findByPlace(id);
		response.setTimes(entryList.size());

		// 魚別の釣果リスト
		Map<Integer, List<EntryFish>> fishListMap = entryList.stream().flatMap(entry -> entry.getFishList().stream())
				.collect(Collectors.groupingBy(EntryFish::getFish));
		response.setFishList(fishListMap.entrySet().stream().map(e -> {
			ShowPlaceFishInfo fishInfo = new ShowPlaceFishInfo();
			fishInfo.setFishName(fishRepository.findById(e.getKey()).map(Fish::getName).orElse(""));
			fishInfo.setCount(
					e.getValue().stream().map(EntryFish::getCount).reduce((accum, value) -> accum + value).orElse(0));
			fishInfo.setMin(e.getValue().stream().map(EntryFish::getMin).min(Comparator.naturalOrder()).orElse(0));
			fishInfo.setMax(e.getValue().stream()
					.map(entryFish -> Objects.nonNull(entryFish.getMax()) ? entryFish.getMax() : entryFish.getMin())
					.max(Comparator.naturalOrder()).orElse(fishInfo.getMin()));
			fishInfo.setMethodList(
					e.getValue().stream().map(entryFish -> methodRepository.findById(entryFish.getMethod()))
							.map(method -> method.map(Method::getName).orElse("")).distinct().sorted()
							.collect(Collectors.toList()));
			return fishInfo;
		}).sorted(Comparator.comparing(ShowPlaceFishInfo::getFishName)).collect(Collectors.toList()));

		// 月毎の魚リスト
		response.setMonthFishList(IntStream.range(1, 13)
				.mapToObj(i -> entryList.stream().filter(entry -> entry.getMonth().equals(i))
						.flatMap(entry -> entry.getFishList().stream())
						.map(entryFish -> fishRepository.findById(entryFish.getFish()))
						.map(fish -> fish.map(Fish::getName).orElse("")).distinct().sorted()
						.collect(Collectors.toList()))
				.collect(Collectors.toList()));

		response.setTideBaseUrl(
				MessageFormat.format(TIDE_URL, place.getTidePc(), place.getTideHc(), "YYYY", "MM", "DD"));

		response.setMonthCountList(IntStream.range(1, 13)
				.mapToObj(i -> (int) entryList.stream().filter(entry -> entry.getMonth().equals(i)).count())
				.collect(Collectors.toList()));

		return response;
	}

	public TopPlaceResponse createTopResponse(Integer month) {
		TopPlaceResponse response = new TopPlaceResponse();
		// @formatter:off
		Specification<Entry> spec = Specification.where(
				EntrySpecs.monthEquals(Optional.ofNullable(month)))
				.and(EntrySpecs.existsAnyFish());
		// @formatter:on
		List<Entry> entryList = entryRepository.findAll(spec);
		Map<Integer, List<Entry>> placeMap = entryList.stream().collect(Collectors.groupingBy(Entry::getPlace));

		List<TopPlace> placeList = placeMap.keySet().stream().map(placeId -> {
			TopPlace top = new TopPlace();
			top.setPlaceId(placeId);
			Place place = placeRepository.findById(placeId).get();
			top.setName(place.getName());
			top.setPrefecture(prefectureRepository.findById(place.getPrefecture()).get().getName());

			List<Entry> placeEntryList = placeMap.get(placeId);
			top.setCount(placeEntryList.size());
			top.setFishNameList(placeEntryList.stream().flatMap(entry -> entry.getFishList().stream())
					.map(entryFish -> fishRepository.findById(entryFish.getFish()))
					.map(fish -> fish.map(Fish::getName).orElse("")).distinct().sorted().collect(Collectors.toList()));

			return top;
		}).sorted(Comparator.comparing(TopPlace::getCount).reversed()).collect(Collectors.toList());

		if (placeList.size() > TOP_PLACE_SIZE)
			placeList = placeList.subList(0, TOP_PLACE_SIZE);
		response.setPlaceList(placeList);

		return response;
	}
}
