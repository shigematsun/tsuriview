package com.example.tsuriview.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageUtils {

	@Value("${aws.url}")
	private String BASE_URL;
	
	@Value("${aws.bucketName}")
	private String BUCKET_NAME;
	
	public String getUrlByKey(String key) {
		return BASE_URL + BUCKET_NAME + "/" + key;
	}
	
	public String getBaseUrl() {
		return BASE_URL + BUCKET_NAME + "/";
	}

}
