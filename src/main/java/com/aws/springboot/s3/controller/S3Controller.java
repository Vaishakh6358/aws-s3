package com.aws.springboot.s3.controller;

import com.aws.springboot.s3.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/s3")
public class S3Controller {

    @Autowired
    private S3Service service;

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).
                body((service.uploadFile(file)));
    }

    @DeleteMapping(value = "/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return ResponseEntity.ok().body((service.deleteFile(fileName)));
    }

    @GetMapping(value = "/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        return ResponseEntity.ok()
                .contentLength(service.downloadFile(fileName).length)
                .header("Content-type","application/octet-stream")
                .header("Content-disposition",
                        "attachment; fileName = \"" + fileName + "\"")
                .body((new ByteArrayResource(service.downloadFile(fileName))));
    }
}
