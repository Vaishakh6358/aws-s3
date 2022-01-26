package com.aws.springboot.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Service
@Slf4j
public class S3Service {

    @Autowired
    private AmazonS3 s3Client;


    @Value("${application.bucket.name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(bucketName, fileName, fileObj);
        fileObj.delete();
        return "Successfully uploaded file : " + fileName;
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            log.error("Error occur while downloading");
            e.printStackTrace();
        }
        return null;

    }

    public String deleteFile(String fileName)
    {
        s3Client.deleteObject(bucketName,fileName);

        return "File deleted : " + fileName;
    }

    private File convertMultiPartToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
            fileOutputStream.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipart file to file");

        }
        return convertedFile;
    }
}
