package com.dotsub.webapp.service;

import com.dotsub.webapp.service.dto.FileDataDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface FileDataService {

    FileDataDTO save(MultipartFile file, Instant creationDate) throws IOException;

    FileDataDTO update(FileDataDTO fileDataDTO);

    void delete(Long id);

    Optional<FileDataDTO> findOne(Long id);

    Optional<FileDataDTO> findByPath(String path);

    List<FileDataDTO> findAll();
}
