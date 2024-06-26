package com.imageupload.servicesImpl;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.imageupload.exceptions.ImageUploadException;
import com.imageupload.services.ImageUploader;
@Service
public class S3ImageUploader implements ImageUploader {
    //inject repositories and services

    @Autowired
    private AmazonS3 client;

    @Value("${app.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadImage(MultipartFile image) {

        if(image==null){
            throw new ImageUploadException("Image is null!!");
        }
        //first create file to upload
        // suppose we have "abc.png" file
        try{
            String actualFileName = image.getOriginalFilename();

                //then we have to generate a id and add with file extension i.e 4527gwh.png
            String fileName = UUID.randomUUID().toString() + actualFileName.substring(actualFileName.lastIndexOf("."));

            //craete metadata
            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentLength(image.getSize());
                
                    //upload image now 
            client.putObject(new PutObjectRequest(bucketName, fileName, image.getInputStream(), metaData));
                   // return fileName;

                   return this.preSignedUrl(fileName);

             } catch(IOException e){
                 throw new ImageUploadException("error in uploading image "+ e.getMessage());
        }
    }

    @Override
    public List<String> allFiles() {

        ListObjectsV2Request listObjectRequest = new ListObjectsV2Request().withBucketName(bucketName);
      
      ListObjectsV2Result listObjectsV2Result = client.listObjectsV2(listObjectRequest);
      List<S3ObjectSummary> objectSummaries= listObjectsV2Result.getObjectSummaries();
      //List<String> listOfFiles = objectSummaries.stream().map(item -> item.getKey()).collect(Collectors.toList());
      
      List<String> listOfUrls = objectSummaries.stream().map(url -> this.preSignedUrl(url.getKey())).collect(Collectors.toList());

      return listOfUrls;
        
    }

    @Override  //if we are authenticate user then aws gives us url to access to object/ It is urk who has access to the object
    public String preSignedUrl(String filename) {

        Date expirationDate = new Date();
        long time = expirationDate.getTime();
        int hour= 2;
        time = time + hour *60 *60 *1000; 
        expirationDate.setTime(time);

      GeneratePresignedUrlRequest generatePresignedUrlRequest= 
                        new GeneratePresignedUrlRequest(bucketName, filename)
                        .withMethod(HttpMethod.GET)
                         .withExpiration(expirationDate); 
     URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
        
    }

    @Override
    public String getImageUrlByName(String filename) {
        S3Object object = client.getObject(bucketName, filename);
        String key = object.getKey();
        String url = preSignedUrl(key);
        //return filename;
        return url;

    }

    
}
