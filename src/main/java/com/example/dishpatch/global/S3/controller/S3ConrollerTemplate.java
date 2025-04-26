package com.example.dishpatch.global.S3.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dishpatch.global.S3.service.S3Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3ConrollerTemplate {

	private final S3Service s3Service;

	@PostMapping("/image")
	public String uploadFile(@RequestParam("file")MultipartFile file){

		try {
			String imageUrl = s3Service.uploadImage(file);
			return "파일이 정상적으로 업로드 되었습니다. url: " + imageUrl;
		} catch (IOException e) {
			e.printStackTrace();
			return "파일 업로드 실패";
		}
	}
}


