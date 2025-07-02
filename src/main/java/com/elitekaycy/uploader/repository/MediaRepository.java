package com.elitekaycy.uploader.repository;

import com.elitekaycy.uploader.model.Media;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
  List<Media> findAllByOrderByUploadDateDesc();

  List<Media> findByDescriptionContainingIgnoreCase(String searchTerm);
}
