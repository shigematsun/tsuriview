package com.example.tsuriview.service;

import java.text.SimpleDateFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.tsuriview.entity.Entry;
import com.example.tsuriview.entity.EntryFish;
import com.example.tsuriview.entity.Fish;
import com.example.tsuriview.entity.Image;
import com.example.tsuriview.entity.Method;
import com.example.tsuriview.entity.Place;
import com.example.tsuriview.entity.Prefecture;
import com.example.tsuriview.form.ShowEntryResponse;
import com.example.tsuriview.form.ShowFishInfo;
import com.example.tsuriview.repository.EntryFishRepository;
import com.example.tsuriview.repository.EntryRepository;
import com.example.tsuriview.repository.FishRepository;
import com.example.tsuriview.repository.ImageRepository;
import com.example.tsuriview.repository.MethodRepository;
import com.example.tsuriview.repository.PlaceRepository;
import com.example.tsuriview.repository.PrefectureRepository;
import com.example.tsuriview.util.ImageUtils;

@Service
public class ShowEntryService {

	@Autowired
	EntryRepository entryRepository;

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	EntryFishRepository entryFishRepository;

	@Autowired
	FishRepository fishRepository;

	@Autowired
	PrefectureRepository prefectureRepository;

	@Autowired
	PlaceRepository placeRepository;

	@Autowired
	MethodRepository methodRepository;

	@Autowired
	ImageUtils imageUtils;

	@Value("${tide.url}")
	private String TIDE_URL;

	private static final String DATE_FORMAT = "yyyy年MM月dd日（E）";

	public ShowEntryResponse createShowResponse(Integer id) {
		ShowEntryResponse response = new ShowEntryResponse();
		List<Image> imageList = imageRepository.findByEntryId(id);
		response.setImageUrlList(
				imageList.stream().map(image -> imageUtils.getUrlByKey(image.getKey())).collect(Collectors.toList()));

		Entry entry = entryRepository.getById(id);

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		response.setDate(sdf.format(entry.getDate()));

		response.setStartTime(entry.getStartTime());
		response.setEndTime(entry.getEndTime());
		response.setMemo(entry.getMemo());

		List<EntryFish> fishList = entry.getFishList();
		fishList.sort(Comparator.comparing(EntryFish::getTimeHour).thenComparing(EntryFish::getTimeMinute));
		response.setTotalFish(
				fishList.stream().map(fish -> fish.getCount()).reduce((accum, value) -> accum + value).orElse(0));
		response.setFishNameList(
				fishList.stream().map(fish -> fish.getFish()).map(fishId -> fishRepository.findById(fishId))
						.map(opt -> opt.map(Fish::getName).orElse("")).distinct().collect(Collectors.toList()));
		response.setFishList(fishList.stream().map(fish -> {
			ShowFishInfo fishInfo = new ShowFishInfo();
			fishInfo.setFishName(fishRepository.findById(fish.getFish()).map(Fish::getName).orElse(""));
			fishInfo.setCount(fish.getCount());
			fishInfo.setMin(fish.getMin());
			fishInfo.setMax(fish.getMax());
			fishInfo.setTime(fish.getTimeHour() + ":" + fish.getTimeMinute());
			fishInfo.setMethodName(methodRepository.findById(fish.getMethod()).map(Method::getName).orElse(""));
			return fishInfo;
		}).collect(Collectors.toList()));

		response.setPrefecture(
				prefectureRepository.findById(entry.getPrefecture()).map(Prefecture::getName).orElse(""));
		Optional<Place> place = placeRepository.findById(entry.getPlace());
		response.setPlace(place.map(Place::getName).orElse(""));
		response.setMapUrl(place.map(Place::getMapUrl).orElse(""));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(entry.getDate());
		response.setTideUrl(MessageFormat.format(TIDE_URL, place.map(Place::getTidePc).orElse(""),
				place.map(Place::getTideHc).orElse(""), String.valueOf(calendar.get(Calendar.YEAR)),
				calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));

		// 日時順で前後のentryを取得
		List<Entry> entryList = entryRepository
				.findAll(Sort.by(Sort.Direction.ASC, "date").and(Sort.by(Sort.Direction.ASC, "startTime")));
		int index = IntStream.range(0, entryList.size()).map(i -> entryList.get(i).getId().equals(id) ? i : -1)
				.filter(i -> i >= 0).min().getAsInt();
		response.setPrevId(index == 0 ? null : entryList.get(index - 1).getId());
		response.setNextId(index == (entryList.size() - 1) ? null : entryList.get(index + 1).getId());

		return response;
	}
}
