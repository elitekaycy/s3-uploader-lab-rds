package com.elitekaycy.uploader.service.s3;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.StorageClass;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

  private final S3Client s3Client;

  @Value("${aws.s3.bucket}")
  private String s3Bucket;

  @Value("${aws.region}")
  private String s3region;

  public List<String> getBucketObjects() {
    ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(s3Bucket).build();
    return s3Client.listObjectsV2(request).contents().stream().map(S3Object::key).toList();
  }

  public List<String> getBucketPresignedUrls() {
    try (S3Presigner presigner =
        S3Presigner.builder()
            .region(Region.of(s3region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()) {

      ListObjectsV2Response response =
          s3Client.listObjectsV2(ListObjectsV2Request.builder().bucket(s3Bucket).build());

      return response.contents().stream()
          .map(S3Object::key)
          .map(
              key -> {
                try {
                  return presigner
                      .presignGetObject(
                          p ->
                              p.signatureDuration(Duration.ofHours(18))
                                  .getObjectRequest(
                                      GetObjectRequest.builder().bucket(s3Bucket).key(key).build()))
                      .url()
                      .toString();
                } catch (Exception e) {
                  log.error("Error generating presigned URL for key: " + key, e);
                  return "error";
                }
              })
          .filter(url -> !url.equals("error"))
          .collect(Collectors.toList());
    } catch (Exception e) {
      log.error("Error in getBucketPresignedUrls", e);
      throw new RuntimeException("Failed to generate presigned URLs", e);
    }
  }

  public String uploadFile(MultipartFile multipartFile) {
    try {
      PutObjectRequest putObjectRequest =
          PutObjectRequest.builder()
              .bucket(s3Bucket)
              .key(multipartFile.getOriginalFilename())
              .contentLength(multipartFile.getSize())
              .storageClass(StorageClass.STANDARD)
              .build();

      s3Client.putObject(
          putObjectRequest, RequestBody.fromBytes(multipartFile.getInputStream().readAllBytes()));
      return multipartFile.getOriginalFilename() + " Uploaded.";
    } catch (IOException e) {
      return e.getMessage();
    }
  }
}
