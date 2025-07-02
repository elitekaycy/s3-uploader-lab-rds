package com.elitekaycy.uploader.controller;

import com.elitekaycy.uploader.model.Media;
import com.elitekaycy.uploader.service.s3.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class S3Controller {

  private final S3Service s3Service;

  @GetMapping("/media")
  public List<Media> getAllMedia() {
    return s3Service.getAllMedia();
  }

  @GetMapping("/media/search")
  public List<Media> searchMedia(@RequestParam String query) {
    return s3Service.searchMedia(query);
  }

  @PostMapping(value = "/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Media uploadFile(
      @RequestParam("file") MultipartFile file, @RequestParam("description") String description) {
    return s3Service.uploadFile(file, description);
  }
}
