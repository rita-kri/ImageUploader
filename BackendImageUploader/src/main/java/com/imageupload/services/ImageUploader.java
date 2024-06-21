package com.imageupload.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    String uploadImage(MultipartFile image);

    List<String> allFiles();

    String preSignedUrl(String filename); //with this we can help to get the file access

    String getImageUrlByName(String filename);

}
