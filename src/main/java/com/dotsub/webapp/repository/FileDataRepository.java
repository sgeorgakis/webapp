package com.dotsub.webapp.repository;

import com.dotsub.webapp.domain.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, Long> {

    Optional<FileData> findByPath(String path);

    void deleteById(Long id);
}
