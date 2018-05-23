package com.dotsub.webapp.service.impl;

import com.dotsub.webapp.config.ApplicationProperties;
import com.dotsub.webapp.config.Constants;
import com.dotsub.webapp.exception.WebAppException;
import com.dotsub.webapp.service.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class LocalStorageServiceImpl implements StorageService {

    private final ApplicationProperties applicationProperties;

    private final Logger log = LoggerFactory.getLogger(LocalStorageServiceImpl.class);

    public LocalStorageServiceImpl(ApplicationProperties applicationProperties) {
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
        File targetFile = new File(FilenameUtils.concat(path, fullName)).getAbsoluteFile();
        if (targetFile.exists()) {
            throw new WebAppException(Constants.ErrorCode.FILE_ALREADY_EXISTS_ERROR.getCode(),
                    Constants.ErrorCode.FILE_ALREADY_EXISTS_ERROR.getMessage());
        }
        file.transferTo(targetFile);
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
