package com.dotsub.webapp.web.rest;

import com.dotsub.webapp.service.FileDataService;
import com.dotsub.webapp.service.dto.FileDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;

@RestController("/api")
public class FileDataController {

    private final FileDataService fileDataService;

    private final Logger log = LoggerFactory.getLogger(FileDataController.class);

    public FileDataController(FileDataService fileDataService) {
        this.fileDataService = fileDataService;
    }

    /**
     * Upload a new file and create a fileData entity
     *
     * @param file the file
     * @return the created fileData entity
     * @throws URISyntaxException
     */
    @PostMapping(path = "/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDataDTO> uploadFile(@RequestParam("file") MultipartFile file) throws URISyntaxException {
        log.debug("Request to upload a new file: {}", file.getOriginalFilename());
        FileDataDTO result = fileDataService.save(file);
        return ResponseEntity.created(new URI("/api/files"))
            .body(result);
    }
}
