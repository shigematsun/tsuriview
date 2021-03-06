package com.example.tsuriview.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.tsuriview.entity.Entry;
import com.example.tsuriview.entity.EntryFish;
import com.example.tsuriview.entity.Fish;
import com.example.tsuriview.entity.FishSpecs;
import com.example.tsuriview.form.FishIcon;
import com.example.tsuriview.form.FishListResponse;
import com.example.tsuriview.form.ShowFishInitResponse;
import com.example.tsuriview.form.ShowFishResponse;
import com.example.tsuriview.form.TopFishResponse;
import com.example.tsuriview.repository.EntryFishRepository;
import com.example.tsuriview.repository.EntryRepository;
import com.example.tsuriview.repository.FishRepository;
import com.example.tsuriview.util.ImageUtils;

@Service
public class ShowFishService {

	@Autowired
	FishRepository fishRepository;
	@Autowired
	EntryRepository entryRepository;
	@Autowired
	EntryFishRepository entryFishRepository;

	@Autowired
	ImageUtils imageUtils;

	@Value("${top.fish.size}")
	private Integer TOP_FISH_SIZE;

	public ShowFishInitResponse createInitResponse(Optional<String> userId) {
		ShowFishInitResponse response = new ShowFishInitResponse();
		Specification<Fish> spec = Specification.where(FishSpecs.existsUserId(userId));
		response.setFishList(fishRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "name")));
		return response;
	}

	public ShowFishResponse createShowResponse(Integer id, Optional<String> userId) {
		ShowFishResponse response = new ShowFishResponse();
		response.setFishId(id);

		Fish fish = fishRepository.findById(id).get();
		response.setName(fish.getName());
		response.setImageUrl(imageUtils.getUrlByKey(fish.getImageKey()));

		List<EntryFish> entryFishList = userId.isPresent() ? entryFishRepository.findByFishAndUserId(id, userId.get())
				: entryFishRepository.findByFish(id);
		response.setTotal(
				entryFishList.stream().map(EntryFish::getCount).reduce((accum, value) -> accum + value).orElse(0));
		response.setMin(entryFishList.stream().map(EntryFish::getMin).min(Comparator.naturalOrder()).orElse(0));
		response.setMax(entryFishList.stream()
				.map(entryFish -> Objects.nonNull(entryFish.getMax()) ? entryFish.getMax() : entryFish.getMin())
				.max(Comparator.naturalOrder()).orElse(response.getMin()));

		response.setMonthCountList(IntStream.range(1, 13)
				.mapToObj(i -> entryFishList.stream().filter(entryFish -> entryFish.getEntry().getMonth().equals(i))
						.map(EntryFish::getCount).reduce((accum, value) -> accum + value).orElse(0))
				.collect(Collectors.toList()));

		response.setTimeCountList(IntStream.range(0, 24)
				.mapToObj(i -> entryFishList.stream()
						.filter(entryFish -> entryFish.getTimeHour().equals(String.format("%02d", i)))
						.map(EntryFish::getCount).reduce((accum, value) -> accum + value).orElse(0))
				.collect(Collectors.toList()));

		return response;
	}

	public TopFishResponse createTopResponse(Integer month, Optional<String> userId) {
		TopFishResponse response = new TopFishResponse();
		List<Entry> entryList = userId.isPresent() ? entryRepository.findByMonthAndUserId(month, userId.get())
				: entryRepository.findByMonth(month);
		// fishId???????????????
		Map<Integer, Integer> fishMap = entryList.stream().flatMap(entry -> entry.getFishList().stream())
				.collect(Collectors.groupingBy(EntryFish::getFish, Collectors.summingInt(EntryFish::getCount)));

		List<FishIcon> fishList = fishMap.keySet().stream().map(fishId -> {
			FishIcon top = new FishIcon();
			top.setFishId(fishId);
			Fish fish = fishRepository.findById(fishId).get();
			top.setName(fish.getName());
			top.setImageUrl(imageUtils.getUrlByKey(fish.getImageKey()));
			top.setCount(fishMap.get(fishId));
			return top;
		}).sorted(Comparator.comparing(FishIcon::getCount).reversed()).collect(Collectors.toList());
		if (fishList.size() > TOP_FISH_SIZE)
			fishList = fishList.subList(0, TOP_FISH_SIZE);
		response.setFishList(fishList);

		return response;
	}

	public FishListResponse createFishListResponse(Optional<String> userId) {
		FishListResponse response = new FishListResponse();
		Specification<Fish> spec = Specification.where(FishSpecs.existsUserId(userId));
		List<Fish> fishList = fishRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "name"));
		response.setFishList(fishList.stream().map(fish -> {
			FishIcon icon = new FishIcon();
			icon.setFishId(fish.getId());
			icon.setName(fish.getName());
			icon.setImageUrl(imageUtils.getUrlByKey(fish.getImageKey()));
			icon.setCount(null);
			return icon;
		}).collect(Collectors.toList()));
		return response;
	}
}
