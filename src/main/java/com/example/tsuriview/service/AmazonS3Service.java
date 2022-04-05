package com.example.tsuriview.service;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

@Service
public class AmazonS3Service {
	@Value("${aws.accessKeyId}")
	private String accessKeyId;

	@Value("${aws.secretAccessKey}")
	private String secretAccessKey;

	@Value("${aws.region}")
	private String region;

	@Value("${aws.bucketName}")
	private String bucketName;

	public String putObject(InputStream fileInputStream, String filename, String contentType, long contentLength) {
		try {
			String key = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now())
					+ "/" + filename; // ファイル名が同じ場合にかぶらないようにキーにdatetimeを使用
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(contentType);
			objectMetadata.setContentLength(contentLength);
			getAmazonS3().putObject(bucketName, key, fileInputStream, objectMetadata);
			return key;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ResponseEntity<InputStreamResource> downloadObject(String key) {
		S3Object s3Object = getAmazonS3().getObject(bucketName, key);
		return ResponseEntity.ok().contentLength(s3Object.getObjectMetadata().getContentLength())
				.contentType(MediaType.parseMediaType(s3Object.getObjectMetadata().getContentType()))
				.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)) // キャッシュ設定
				.body(new InputStreamResource(s3Object.getObjectContent()));
	}

	private AmazonS3 getAmazonS3() {
		return AmazonS3ClientBuilder.standard()
				.withCredentials(
						new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
				.withRegion(region).build();
	}
}