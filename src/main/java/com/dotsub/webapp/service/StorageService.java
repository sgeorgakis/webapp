package com.dotsub.webapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String save(MultipartFile file);

    boolean delete(String path);
}
