package com.dotsub.webapp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    String save(MultipartFile file) throws IOException;

    boolean delete(String path);

}
