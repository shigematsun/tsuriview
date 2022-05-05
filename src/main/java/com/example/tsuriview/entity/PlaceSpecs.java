package com.example.tsuriview.entity;

import java.util.Optional;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

public class PlaceSpecs {

	private PlaceSpecs() {
	}

	public static Specification<Place> existsUserId(Optional<String> userId) {
		return userId.isPresent() ? (root, query, builder) -> {
			Subquery<Integer> subquery = query.subquery(Integer.class);
			Root<Entry> subRoot = subquery.from(Entry.class);
			return builder.exists(
			// @formatter:off
					subquery.select(builder.literal(1)).where(
							builder.equal(root, subRoot.get("place")),
							builder.equal(subRoot.get("userId"), userId.get())
							));
			// @formatter:on
		} : null;
	}
}
