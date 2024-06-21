package com.imageupload.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imageupload.servicesImpl.S3ImageUploader;;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/s3")
public class S3Controller {

    @Autowired
    private S3ImageUploader uploader;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file) {
            // String response = uploader.uploadImage(file);
            // return ResponseEntity.ok(CustomResponse.builder()
            //     .message("Image uploaded successfully")
            //     .success(true)
            //     .data(response)
            //     .build());

            return ResponseEntity.ok(uploader.uploadImage(file));
        
    }

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllFiles(){
        return ResponseEntity.ok(uploader.allFiles());
    }
    @GetMapping("/{filename}")
    public ResponseEntity<?> getUrlByName(@PathVariable String filename){
        return ResponseEntity.ok(uploader.getImageUrlByName(filename));
    }


    @GetMapping("/hello")
    public String msg(){
        return "Hello Postman";
    }

    

}
