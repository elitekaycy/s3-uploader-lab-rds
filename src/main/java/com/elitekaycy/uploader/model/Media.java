package com.elitekaycy.uploader.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Media {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 512)
  private String fileName;

  @Column(nullable = false, length = 2048)
  private String fileUrl;

  @Column(nullable = false, length = 1024)
  private String description;

  @Column(nullable = false)
  private LocalDateTime uploadDate;

  @Column(nullable = false)
  private Long fileSize;

  @Column(nullable = false, length = 128)
  private String fileType;
}
