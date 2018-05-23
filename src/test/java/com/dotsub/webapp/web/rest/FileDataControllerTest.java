package com.dotsub.webapp.web.rest;

import com.dotsub.webapp.WebApp;
import com.dotsub.webapp.service.FileDataService;
import com.dotsub.webapp.service.mapper.FileDataMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.UserDefinedFileAttributeView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApp.class)
public class FileDataControllerTest {

    private static final String TITLE_VALUE = "testTitle";

    private static final String DESCRIPTION_VALUE = "testDescription";
    @Autowired
    private FileDataMapper fileDataMapper;

    @Autowired
    private FileDataService fileDataService;

    @Autowired
    private HttpMessageConverter[] httpMessageConverters;

    private MockMvc restMvc;

    private File file;

    @Before
    public void setup() {

        FileDataController fileDataController = new FileDataController(fileDataMapper, fileDataService);

        this.restMvc = MockMvcBuilders.standaloneSetup(fileDataController)
                .setMessageConverters(httpMessageConverters)
                .build();
    }

    @Ignore
    @Before
    public void addAttributes() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        file = new File(classLoader.getResource("image.jpeg").getFile());
        Files.setAttribute(file.toPath(), "user:title", Charset.defaultCharset().encode(TITLE_VALUE));
        Files.setAttribute(file.toPath(), "user:description", Charset.defaultCharset().encode(DESCRIPTION_VALUE));
    }

    @Ignore
    @Test
    public void uploadFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), MULTIPART_FORM_DATA_VALUE, Files.readAllBytes(file.toPath()));
        assertThat(multipartFile).isNotNull();

        restMvc.perform(multipart("/api/files/upload")
                .file(multipartFile)
                .accept(MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(TITLE_VALUE))
                .andExpect(jsonPath("$.description").value(DESCRIPTION_VALUE));
    }
}
