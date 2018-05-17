package com.dotsub.webapp.web.rest;

import com.dotsub.webapp.service.FileDataService;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class FileDataController {

    private final FileDataService fileDataService;

    public FileDataController(FileDataService fileDataService) {
        this.fileDataService = fileDataService;
    }
}
