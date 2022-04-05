package com.example.tsuriview.form;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ShowEntriesRequest {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fromDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date toDate;

	private Integer month;

	private String prefecture;

	private Integer place;

	private Integer fish;

	private Integer method;

	private Integer page;

}
