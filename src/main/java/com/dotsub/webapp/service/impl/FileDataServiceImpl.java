package com.dotsub.webapp.service.impl;

import com.dotsub.webapp.domain.FileData;
import com.dotsub.webapp.repository.FileDataRepository;
import com.dotsub.webapp.service.FileDataService;
import com.dotsub.webapp.service.dto.FileDataDTO;
import com.dotsub.webapp.service.mapper.FileDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class FileDataServiceImpl implements FileDataService {

    private final Logger log = LoggerFactory.getLogger(FileDataServiceImpl.class);

    private final FileDataRepository fileDataRepository;

    private final FileDataMapper mapper;

    public FileDataServiceImpl(FileDataRepository fileDataRepository, FileDataMapper mapper) {
        this.fileDataRepository = fileDataRepository;
        this.mapper = mapper;
    }

    @Override
    public FileDataDTO save(MultipartFile file) {
        return null;
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
        return fileDataRepository.findByPath(path)
                .map(mapper::toDto);
    }

    @Override
    public List<FileDataDTO> findAll() {
        return mapper.toDto(fileDataRepository.findAll());
    }
}
