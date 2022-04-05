package com.example.tsuriview.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.tsuriview.entity.Entry;
import com.example.tsuriview.entity.EntryFish;
import com.example.tsuriview.entity.Fish;
import com.example.tsuriview.form.ShowFishInitResponse;
import com.example.tsuriview.form.ShowFishResponse;
import com.example.tsuriview.form.TopFish;
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

	public ShowFishInitResponse createInitResponse() {
		ShowFishInitResponse response = new ShowFishInitResponse();
		response.setFishList(fishRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
		return response;
	}

	public ShowFishResponse createShowResponse(Integer id) {
		ShowFishResponse response = new ShowFishResponse();
		response.setFishId(id);

		Fish fish = fishRepository.findById(id).get();
		response.setName(fish.getName());
		response.setImageUrl(imageUtils.getUrlByKey(fish.getImageKey()));

		List<EntryFish> entryFishList = entryFishRepository.findByFish(id);
		response.setTotal(
				entryFishList.stream().map(EntryFish::getCount).reduce((accum, value) -> accum + value).orElse(0));
		response.setMin(entryFishList.stream().map(EntryFish::getMin).min(Comparator.naturalOrder()).orElse(0));
		response.setMax(entryFishList.stream().map(EntryFish::getMax).filter(Objects::nonNull)
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

	public TopFishResponse createTopResponse(Integer month) {
		TopFishResponse response = new TopFishResponse();
		List<Entry> entryList = entryRepository.findByMonth(month);
		// fishIdと魚別の数
		Map<Integer, Integer> fishMap = entryList.stream().flatMap(entry -> entry.getFishList().stream())
				.collect(Collectors.groupingBy(EntryFish::getFish, Collectors.summingInt(EntryFish::getCount)));

		List<TopFish> fishList = fishMap.keySet().stream().map(fishId -> {
			TopFish top = new TopFish();
			top.setFishId(fishId);
			Fish fish = fishRepository.findById(fishId).get();
			top.setName(fish.getName());
			top.setImageUrl(imageUtils.getUrlByKey(fish.getImageKey()));
			top.setCount(fishMap.get(fishId));
			return top;
		}).sorted(Comparator.comparing(TopFish::getCount).reversed()).collect(Collectors.toList());
		if (fishList.size() > TOP_FISH_SIZE)
			fishList = fishList.subList(0, TOP_FISH_SIZE);
		response.setFishList(fishList);

		return response;
	}
}
