package com.example.tsuriview.form;

import java.util.List;

import lombok.Data;

@Data
public class ShowEntriesResponse {
	
	private List<ShowEntriesEntryInfo> entryList;
	
	private Integer totalPages;

}
