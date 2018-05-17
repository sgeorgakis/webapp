package com.dotsub.webapp.service.impl;

import com.dotsub.webapp.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalStorageService implements StorageService {

    //TODO: implement methods

    @Override
    public String save(MultipartFile file) {
        return null;
    }

    @Override
    public boolean delete(String path) {
        return false;
    }
}
