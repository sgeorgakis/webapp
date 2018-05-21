package com.dotsub.webapp.web.rest;

import com.dotsub.webapp.service.FileDataService;
import com.dotsub.webapp.service.mapper.FileDataMapper;
import com.dotsub.webapp.web.rest.view.FileDataView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

@RestController
@RequestMapping("/api")
public class FileDataController {

    private final FileDataMapper mapper;

    private final FileDataService fileDataService;

    private final Logger log = LoggerFactory.getLogger(FileDataController.class);

    public FileDataController(FileDataMapper mapper, FileDataService fileDataService) {
        this.mapper = mapper;
        this.fileDataService = fileDataService;
    }

    /**
     * Upload a new file and create a fileData entity
     *
     * @param file the file
     * @return the created fileData entity
     * @throws URISyntaxException
     * @throws IOException
     */
    @PostMapping(path = "/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDataView> uploadFile(@RequestPart("file") MultipartFile file, @RequestPart("creationDate") Instant creationDate) throws URISyntaxException, IOException {
        log.debug("Request to upload a new file: {}", file.getOriginalFilename());
        FileDataView result = mapper.toView(fileDataService.save(file, creationDate));
        return ResponseEntity.created(new URI("/api/files"))
            .body(result);
    }
}
