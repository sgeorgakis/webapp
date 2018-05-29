package com.dotsub.webapp.web.rest;

import com.dotsub.webapp.WebApp;
import com.dotsub.webapp.service.FileDataService;
import com.dotsub.webapp.service.StorageService;
import com.dotsub.webapp.service.mapper.FileDataMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.File;
import java.io.InputStream;

import static com.dotsub.webapp.config.Constants.ErrorCode.FILE_ALREADY_EXISTS_ERROR;
import static com.dotsub.webapp.config.Constants.ErrorCode.INVALID_ARGUMENTS_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApp.class)
public class FileDataControllerTest {

    private static final String TITLE_REQUEST_PART = "title";

    private static final String DESCRIPTION_REQUEST_PART = "description";

    private static final String FILE_REQUEST_PART = "file";

    private static final String TITLE_VALUE = "testTitle";

    private static final String DESCRIPTION_VALUE = "testDescription";

    private static final String FILE = "image.jpeg";

    @Autowired
    private FileDataMapper fileDataMapper;

    @Autowired
    private FileDataService fileDataService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private HttpMessageConverter[] httpMessageConverters;

    @Autowired
    private ResponseEntityExceptionHandler exceptionHandler;

    private MockMvc restMvc;

    private File file;

    @Before
    public void setup() {

        FileDataController fileDataController = new FileDataController(fileDataMapper, fileDataService);

        this.restMvc = MockMvcBuilders.standaloneSetup(fileDataController)
                .setMessageConverters(httpMessageConverters)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    public void uploadFile() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream file = classLoader.getResourceAsStream(FILE)) {

            MockMultipartFile multipartFile = new MockMultipartFile(FILE_REQUEST_PART, FILE, MULTIPART_FORM_DATA_VALUE, file);
            assertThat(multipartFile).isNotNull();

            MockPart title = new MockPart(TITLE_REQUEST_PART, TITLE_REQUEST_PART, TITLE_VALUE.getBytes());
            title.getHeaders().setContentType(MULTIPART_FORM_DATA);

            MockPart description = new MockPart(DESCRIPTION_REQUEST_PART, DESCRIPTION_REQUEST_PART, DESCRIPTION_VALUE.getBytes());
            description.getHeaders().setContentType(MULTIPART_FORM_DATA);

            restMvc.perform(multipart("/api/file-data/upload")
                    .file(multipartFile)
                    .part(title, description)
                    .accept(APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.title").value(TITLE_VALUE))
                    .andExpect(jsonPath("$.description").value(DESCRIPTION_VALUE))
                    .andExpect(jsonPath("$.creationDate").exists());
        }
    }

    @Test
    public void uploadWithoutTitle() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream file = classLoader.getResourceAsStream(FILE)) {

        MockMultipartFile multipartFile = new MockMultipartFile(FILE_REQUEST_PART, FILE, MULTIPART_FORM_DATA_VALUE, file);
        assertThat(multipartFile).isNotNull();

        MockPart description = new MockPart(DESCRIPTION_REQUEST_PART, DESCRIPTION_REQUEST_PART, DESCRIPTION_VALUE.getBytes());
        description.getHeaders().setContentType(MULTIPART_FORM_DATA);

        restMvc.perform(multipart("/api/file-data/upload")
                .file(multipartFile)
                .part(description)
                .accept(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.code").value(INVALID_ARGUMENTS_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(INVALID_ARGUMENTS_ERROR.getMessage()));
        }
    }

    @Test
    public void uploadExistingFile() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream file = classLoader.getResourceAsStream(FILE)) {

            MockMultipartFile multipartFile = new MockMultipartFile(FILE_REQUEST_PART, FILE, MULTIPART_FORM_DATA_VALUE, file);
            assertThat(multipartFile).isNotNull();

            fileDataService.save(multipartFile, TITLE_VALUE, DESCRIPTION_VALUE);

            MockPart title = new MockPart(TITLE_REQUEST_PART, TITLE_REQUEST_PART, TITLE_VALUE.getBytes());
            title.getHeaders().setContentType(MULTIPART_FORM_DATA);

            MockPart description = new MockPart(DESCRIPTION_REQUEST_PART, DESCRIPTION_REQUEST_PART, DESCRIPTION_VALUE.getBytes());
            description.getHeaders().setContentType(MULTIPART_FORM_DATA);

            restMvc.perform(multipart("/api/file-data/upload")
                    .file(multipartFile)
                    .part(title, description)
                    .accept(APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.code").value(FILE_ALREADY_EXISTS_ERROR.getCode()))
                    .andExpect(jsonPath("$.message").value(FILE_ALREADY_EXISTS_ERROR.getMessage()));
        }
    }
}
