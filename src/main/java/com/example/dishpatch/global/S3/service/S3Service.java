package com.example.dishpatch.global.S3.service;

import java.io.IOException;
import java.net.UnknownServiceException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.dishpatch.domain.user.exception.UserErrorCode;
import com.example.dishpatch.global.exception.BizException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {
	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String uploadImage(MultipartFile image) throws IOException {
		String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

		// 메타데이터 설정
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(image.getContentType());
		metadata.setContentLength(image.getSize());

		// S3에 파일 업로드 요청 생성
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, image.getInputStream(), metadata);

		String originalFilename = image.getOriginalFilename();
		String extension = "";

		if (originalFilename != null && originalFilename.contains(".")) {
			extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		}

		if(!extension.equals("jpg")&&!extension.equals("jpeg")&&!extension.equals("png")&&!extension.equals("jfif")){
			throw new BizException(UserErrorCode.INVALID_FILE);
		}


		//  S3에 파일 업로드
		amazonS3.putObject(putObjectRequest);

		return getPublicUrl(fileName);
	}

	private String getPublicUrl(String fileName){
		return amazonS3.getUrl(bucket, fileName).toString();
	}
}
