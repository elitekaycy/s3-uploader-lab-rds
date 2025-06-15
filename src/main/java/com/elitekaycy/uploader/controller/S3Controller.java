package com.elitekaycy.uploader.controller;

import com.elitekaycy.uploader.service.s3.S3Service;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class S3Controller {

  private final S3Service s3Service;

  @GetMapping("/s3/bucket/all")
  public List<String> getBucketObjects() {
    return s3Service.getBucketPresignedUrls();
  }

  @PostMapping("/s3")
  public List<String> uploadFile(@RequestParam("file") List<MultipartFile> multipartFiles) {
    return multipartFiles.stream().map(s3Service::uploadFile).collect(Collectors.toList());
  }

  /**
   * @DeleteMapping public void deleteFile(@RequestParam String fileName) {
   * s3Service.deleteFile(fileName); }
   */
}
