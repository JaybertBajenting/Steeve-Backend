package com.example.template.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class DigitalOceanService {


    private final AmazonS3 amazonS3;


    @Value("${spring.digitalocean.bucketName}")
    private String bucketName;



    public void uploadFile(String key, File file){
            amazonS3.putObject(new PutObjectRequest(bucketName,key,file).withCannedAcl(CannedAccessControlList.PublicRead));
    }



    public InputStream downloadFile(String key){
       S3Object object = amazonS3.getObject(bucketName,key);
        return object.getObjectContent();
    }


    public void deleteFile(String key){
         amazonS3.deleteObject(bucketName,key);
    }


    public void deleteFileByUrl(String url){
            try{
                URL s3Url = new URL(url);
                String key = s3Url.getPath().substring(bucketName.length() + 2);
                amazonS3.deleteObject(bucketName, key);
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
    }

}
