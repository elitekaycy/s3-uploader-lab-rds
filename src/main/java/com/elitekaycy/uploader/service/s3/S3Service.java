package com.elitekaycy.uploader.service.s3;

import com.elitekaycy.uploader.model.Media;
import com.elitekaycy.uploader.repository.MediaRepository;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
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
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.StorageClass;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

  private final S3Client s3Client;
  private final MediaRepository mediaRepository;

  @Value("${aws.s3.bucket}")
  private String s3Bucket;

  @Value("${aws.region}")
  private String s3region;

  public List<Media> getAllMedia() {
    return mediaRepository.findAllByOrderByUploadDateDesc();
  }

  public List<Media> searchMedia(String searchTerm) {
    return mediaRepository.findByDescriptionContainingIgnoreCase(searchTerm);
  }

  public Media uploadFile(MultipartFile multipartFile, String description) {
    try {
      String fileName = multipartFile.getOriginalFilename();
      String fileType = multipartFile.getContentType();
      long fileSize = multipartFile.getSize();

      PutObjectRequest putObjectRequest =
          PutObjectRequest.builder()
              .bucket(s3Bucket)
              .key(fileName)
              .contentLength(fileSize)
              .contentType(fileType)
              .storageClass(StorageClass.STANDARD)
              .build();

      s3Client.putObject(
          putObjectRequest, RequestBody.fromBytes(multipartFile.getInputStream().readAllBytes()));

      String fileUrl = generatePresignedUrl(fileName);

      Media media = new Media();
      media.setFileName(fileName);
      media.setFileUrl(fileUrl);
      media.setDescription(description);
      media.setUploadDate(LocalDateTime.now());
      media.setFileSize(fileSize);
      media.setFileType(fileType);

      return mediaRepository.save(media);
    } catch (IOException e) {
      throw new RuntimeException("Failed to upload file", e);
    }
  }

  private String generatePresignedUrl(String fileName) {
    try (S3Presigner presigner =
        S3Presigner.builder()
            .region(Region.of(s3region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()) {

      GetObjectRequest getObjectRequest =
          GetObjectRequest.builder().bucket(s3Bucket).key(fileName).build();

      GetObjectPresignRequest getObjectPresignRequest =
          GetObjectPresignRequest.builder()
              .signatureDuration(Duration.ofHours(24))
              .getObjectRequest(getObjectRequest)
              .build();

      return presigner.presignGetObject(getObjectPresignRequest).url().toString();
    }
  }
}
