package com.example.tsuriview.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class EntryFishKey implements Serializable{

	/**
	 * 生成シリアルバージョンID
	 */
	private static final long serialVersionUID = -7341934199331592662L;

	private Integer entryId;

	private Integer index;
}