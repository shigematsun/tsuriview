package com.example.tsuriview.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.tsuriview.entity.Entry;
import com.example.tsuriview.entity.EntryFish;
import com.example.tsuriview.entity.Image;
import com.example.tsuriview.entity.Place;
import com.example.tsuriview.form.EditEntryForm;
import com.example.tsuriview.form.EditEntryInitResponse;
import com.example.tsuriview.form.FishInfo;
import com.example.tsuriview.form.ImageInfo;
import com.example.tsuriview.repository.EntryFishRepository;
import com.example.tsuriview.repository.EntryRepository;
import com.example.tsuriview.repository.FishRepository;
import com.example.tsuriview.repository.ImageRepository;
import com.example.tsuriview.repository.MethodRepository;
import com.example.tsuriview.repository.PlaceRepository;
import com.example.tsuriview.repository.PrefectureRepository;
import com.example.tsuriview.util.ImageUtils;

@Service
public class EditEntryService {

	@Autowired
	PrefectureRepository prefectureRepository;
	@Autowired
	PlaceRepository placeRepository;
	@Autowired
	FishRepository fishRepository;
	@Autowired
	MethodRepository methodRepository;
	@Autowired
	EntryRepository entryRepository;
	@Autowired
	EntryFishRepository entryFishRepository;
	@Autowired
	ImageRepository imageRepository;

	@Autowired
	ImageUtils imageUtils;

	public EditEntryInitResponse createInitResponse(Optional<Integer> id) {
		EditEntryInitResponse response = new EditEntryInitResponse();
		response.setPrefectureList(prefectureRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
		List<Place> placeList = placeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
		// 場所を都道府県ごとのマップに変換
		Map<String, List<Place>> placeListMap = placeList.stream().collect(Collectors.groupingBy(Place::getPrefecture));
		response.setPlaceListMap(placeListMap);
		response.setFishList(fishRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
		response.setMethodList(methodRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));

		EditEntryForm entry = new EditEntryForm();
		id.ifPresent(idVal -> {
			Entry data = entryRepository.findById(idVal).get();
			entry.setId(Optional.of(data.getId()));
			entry.setDate(data.getDate());
			entry.setStartTime(data.getStartTime());
			entry.setEndTime(data.getEndTime());
			entry.setPrefecture(data.getPrefecture());
			entry.setPlace(data.getPlace());
			entry.setMemo(data.getMemo());

			entry.setFishInfoList(data.getFishList().stream().map(fish -> {
				FishInfo info = new FishInfo();
				info.setFish(fish.getFish());
				info.setCount(fish.getCount());
				info.setMin(fish.getMin());
				info.setMax(fish.getMax());
				info.setMethod(fish.getMethod());
				info.setTime(fish.getTimeHour() + ":" + fish.getTimeMinute());
				return info;
			}).collect(Collectors.toList()));

			List<Image> imageList = imageRepository.findByEntryId(idVal);
			entry.setImageList(imageList.stream().map(image -> {
				ImageInfo info = new ImageInfo();
				info.setId(image.getId());
				info.setUrl(imageUtils.getUrlByKey(image.getKey()));
				return info;
			}).collect(Collectors.toList()));
			;
		});
		response.setEntry(entry);

		return response;
	}

	public String editEntry(EditEntryForm request, String userId) {
		Entry entry = request.getId().map(id -> entryRepository.findById(id).get()).orElse(new Entry());
		entry.setDate(request.getDate());
		entry.setStartTime(request.getStartTime());
		entry.setEndTime(request.getEndTime());
		entry.setPrefecture(request.getPrefecture());
		entry.setPlace(request.getPlace());
		entry.setMemo(request.getMemo());
		entry.setUserId(userId);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(request.getDate());
		entry.setMonth(calendar.get(Calendar.MONTH) + 1);

		Entry entryResult = entryRepository.save(entry);
		Integer entryId = entryResult.getId();

		request.getId().ifPresent(id -> {
			entryFishRepository.deleteByEntryId(id);
			List<Image> oldImageList = imageRepository.findByEntryId(id);
			oldImageList.stream().forEach(image -> {
				image.setEntryId(null);
				image.setEntryIndex(null);
			});
			imageRepository.saveAll(oldImageList);
		});

		if (!request.getFishInfoList().isEmpty()) {
			List<EntryFish> fishList = new ArrayList<EntryFish>();
			int i = 1;
			for (FishInfo fishInfo : request.getFishInfoList()) {
				EntryFish entryFish = new EntryFish();
				entryFish.setEntryId(entryId);
				entryFish.setIndex(i++);
				entryFish.setFish(fishInfo.getFish());
				entryFish.setCount(fishInfo.getCount());
				entryFish.setMin(fishInfo.getMin());
				entryFish.setMax(fishInfo.getMax());
				entryFish.setMethod(fishInfo.getMethod());
				entryFish.setTimeHour(fishInfo.getTime().split(":")[0]);
				entryFish.setTimeMinute(fishInfo.getTime().split(":")[1]);
				entryFish.setUserId(userId);
				fishList.add(entryFish);
			}
			entryFishRepository.saveAll(fishList);
		}

		if (!request.getImageList().isEmpty()) {
			List<Image> imageList = new ArrayList<Image>();
			int j = 1;
			for (ImageInfo imageInfo : request.getImageList()) {
				Image image = imageRepository.findById(imageInfo.getId()).get();
				image.setEntryId(entryId);
				image.setEntryIndex(j++);
				imageList.add(image);
			}
			imageRepository.saveAll(imageList);
		}
		return "成功しました";
	}
}
