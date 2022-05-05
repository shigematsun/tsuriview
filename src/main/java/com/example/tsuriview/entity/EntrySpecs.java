package com.example.tsuriview.entity;

import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.Optional;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

public class EntrySpecs {

	private EntrySpecs() {
	}

	public static Specification<Entry> dateAfter(Optional<Date> date) {
		return date.isPresent() ? (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("date"), date.get())
				: null;
	}

	public static Specification<Entry> dateBefore(Optional<Date> date) {
		return date.isPresent() ? (root, query, builder) -> builder.lessThanOrEqualTo(root.get("date"), date.get())
				: null;
	}

	public static Specification<Entry> monthEquals(Optional<Integer> month) {
		return month.isPresent() ? (root, query, builder) -> builder.equal(root.get("month"), month.get()) : null;
	}

	public static Specification<Entry> prefectureEquals(Optional<String> prefecture) {
		return prefecture.isPresent()
				? (root, query, builder) -> builder.equal(root.get("prefecture"), prefecture.get())
				: null;
	}

	public static Specification<Entry> placeEquals(Optional<Integer> place) {
		return place.isPresent() ? (root, query, builder) -> builder.equal(root.get("place"), place.get()) : null;
	}

	public static Specification<Entry> userIdEquals(Optional<String> userId) {
		return userId.isPresent() ? (root, query, builder) -> builder.equal(root.get("userId"), userId.get()) : null;
	}

	public static Specification<Entry> existsFish(Optional<Integer> fish) {
		return fish.isPresent() ? (root, query, builder) -> {
			Subquery<Integer> subquery = query.subquery(Integer.class);
			Root<EntryFish> subRoot = subquery.from(EntryFish.class);
			return builder.exists(
			// @formatter:off
					subquery.select(builder.literal(1)).where(
							builder.equal(root, subRoot.get("entry").get("id")),
							builder.equal(subRoot.get("fish"), fish.get())
							));
			// @formatter:on
		} : null;
	}

	public static Specification<Entry> existsAnyFish() {
		return (root, query, builder) -> {
			Subquery<Integer> subquery = query.subquery(Integer.class);
			Root<EntryFish> subRoot = subquery.from(EntryFish.class);
			return builder.exists(
			// @formatter:off
					subquery.select(builder.literal(1)).where(
							builder.equal(root, subRoot.get("entry").get("id"))
							));
			// @formatter:on
		};
	}

	public static Specification<Entry> existsMethod(Optional<Integer> method) {
		return method.isPresent() ? (root, query, builder) -> {
			Subquery<Integer> subquery = query.subquery(Integer.class);
			Root<EntryFish> subRoot = subquery.from(EntryFish.class);
			return builder.exists(
			// @formatter:off
					subquery.select(builder.literal(1)).where(
							builder.equal(root, subRoot.get("entry").get("id")),
							builder.equal(subRoot.get("method"), method.get())
							));
			// @formatter:on
		} : null;
	}
}
