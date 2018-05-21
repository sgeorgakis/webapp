package com.dotsub.webapp.service.impl;

import com.dotsub.webapp.config.Constants;
import com.dotsub.webapp.domain.FileData;
import com.dotsub.webapp.repository.FileDataRepository;
import com.dotsub.webapp.service.FileDataService;
import com.dotsub.webapp.service.StorageService;
import com.dotsub.webapp.service.dto.FileDataDTO;
import com.dotsub.webapp.service.mapper.FileDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.dotsub.webapp.config.Constants.MetaData.DESCRIPTION;
import static com.dotsub.webapp.config.Constants.MetaData.TITLE;

@Service
public class FileDataServiceImpl implements FileDataService {

    private final Logger log = LoggerFactory.getLogger(FileDataServiceImpl.class);

    private final FileDataRepository fileDataRepository;

    private final FileDataMapper mapper;

    private final StorageService storageService;

    public FileDataServiceImpl(FileDataRepository fileDataRepository, FileDataMapper mapper,
                               StorageService storageService) {
        this.fileDataRepository = fileDataRepository;
        this.mapper = mapper;
        this.storageService = storageService;
    }

    /**
     * Save the file, create new fileData entity
     * and persist it to the database
     *
     * @param file the file to persist
     * @return the new fileData persisted entity
     */
    @Override
    public FileDataDTO save(MultipartFile file, Instant creationDate) throws IOException {
        log.debug("Request to save new file: {}", file.getOriginalFilename());
        String path = null;
        try {
            path = storageService.save(file);
            FileDataDTO fileDataDTO = getFileMetadata(path);
            fileDataDTO.setCreationDate(creationDate);
            FileData entity = mapper.toEntity(fileDataDTO);
            return mapper.toDto(fileDataRepository.save(entity));
        } catch (Exception e) {
            log.error("Message: {}", e.getMessage());
            if (path != null) {
                storageService.delete(path);
            }
            throw e;
        }
    }

    /**
     * Update a fileData entity
     *
     * @param fileDataDTO the entity to update
     * @return the persisted entity
     */
    @Override
    public FileDataDTO update(FileDataDTO fileDataDTO) {
        log.debug("Request to update fileData with id: {}", fileDataDTO.getId());
        FileData entity = mapper.toEntity(fileDataDTO);
        return mapper.toDto(fileDataRepository.save(entity));
    }

    /**
     * Delete the fileData entity
     * with the given id
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete fileData with id: {}", id);
        fileDataRepository.deleteById(id);
    }

    /**
     * Find a fileData entity
     * with the given id
     *
     * @param id the id of the entity to search
     * @return the found entity or an empty Optional wrapper
     */
    @Override
    public Optional<FileDataDTO> findOne(Long id) {
        log.debug("Request to find fileData with id: {}", id);
        return fileDataRepository.findById(id)
                .map(mapper::toDto);
    }

    /**
     * Find a fileData entity
     * with the given save path
     *
     * @param path the path of the entity to search
     * @return the found entity or an empty Optional wrapper
     */
    @Override
    public Optional<FileDataDTO> findByPath(String path) {
        log.debug("Request to find fileData in path: {}", path);
        return fileDataRepository.findByPath(path)
                .map(mapper::toDto);
    }

    /**
     * Find all the fileData entities in database
     *
     * @return a list with all the fileData entities
     */
    @Override
    public List<FileDataDTO> findAll() {
        log.debug("Request to find all fileDatas");
        return mapper.toDto(fileDataRepository.findAll());
    }

    /**
     * Create the corresponding fileData entity
     * using the path to the uploaded file
     *
     * @param path the path to the file
     * @return the created fileData entity
     * @throws IOException
     */
    private FileDataDTO getFileMetadata(String path) throws IOException {
        FileDataDTO fileData = new FileDataDTO();
        Path filePath = Paths.get(path);
        String title = (String) getUserDefinedAttribute(filePath, TITLE);
        String description = (String) getUserDefinedAttribute(filePath, DESCRIPTION);

        fileData.setTitle(title);
        fileData.setDescription(description);
        return fileData;
    }

    /**
     * Get the requested metadata of the file.
     * If it does not exist, return null
     *
     * @param path the path to the file
     * @param attribute the attribute to get the value
     * @return the attribute's value
     * @throws IOException
     */
    private Object getUserDefinedAttribute(Path path, Constants.MetaData attribute) throws IOException {
        try {
            return Files.getAttribute(path, attribute.getValue());
        } catch (IllegalArgumentException e) {
            log.warn("Exception occured when trying to get attribute {}", attribute.getValue());
            log.warn("Message: {}", e.getMessage());
            return null;
        }
    }
}
