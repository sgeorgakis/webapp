package com.dotsub.webapp.service.impl;

import com.dotsub.webapp.config.ApplicationProperties;
import com.dotsub.webapp.service.StorageService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class LocalStorageService implements StorageService {

    private final ApplicationProperties applicationProperties;

    public LocalStorageService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save the file to the specified folder
     * and return its full path
     *
     * @param file the file to save
     * @return the path of the saved file
     * @throws IOException
     */
    @Override
    public String save(MultipartFile file) throws IOException {
        String fullName = file.getOriginalFilename();
        String path = applicationProperties.getUpload().getSaveFolder();
        File targetFile = new File(FilenameUtils.concat(path, fullName));
        if (targetFile.exists()) {
            throw new RuntimeException("File name already exists. Rename the file and try again.");
        }
        FileUtils.writeByteArrayToFile(targetFile, file.getBytes());
        return targetFile.getPath();
    }

    /**
     * Delete the file in the path
     *
     * @param path the path of the file
     * @return true if the file was deleted, false if not
     */
    @Override
    public boolean delete(String path) {
        File targetFile = new File(path);
        return targetFile.delete();
    }
}
