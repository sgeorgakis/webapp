package com.dotsub.webapp.service.mapper;

import com.dotsub.webapp.domain.FileData;
import com.dotsub.webapp.service.dto.FileDataDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileDataMapper {

    FileData toEntity(FileDataDTO dto);

    FileDataDTO toDto(FileData entity);

    List<FileData> toEntity(List<FileDataDTO> dto);

    List<FileDataDTO> toDto(List<FileData> entity);
}
