package com.dotsub.webapp.web.rest;

import com.dotsub.webapp.WebApp;
import com.dotsub.webapp.service.FileDataService;
import com.dotsub.webapp.service.mapper.FileDataMapper;
import org.junit.Before;
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

    @Before
    public void addAttributes() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        file = new File(classLoader.getResource("image.jpeg").getFile());
        UserDefinedFileAttributeView userView = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);

        final byte[] titleBytes = TITLE_VALUE.getBytes("UTF-8");
        final byte[] descriptionBytes = DESCRIPTION_VALUE.getBytes("UTF-8");
        final ByteBuffer titleBuffer = ByteBuffer.allocate(titleBytes.length);
        final ByteBuffer descriptionBuffer = ByteBuffer.allocate(descriptionBytes.length);
        titleBuffer.put(titleBytes);
        descriptionBuffer.put(descriptionBytes);
        titleBuffer.flip();
        descriptionBuffer.flip();

        userView.write("user:title", titleBuffer);
        userView.write("user:description",descriptionBuffer);

//        Files.setAttribute(file.toPath(), "user:title", ByteBuffer.wrap(TITLE_VALUE.getBytes()));
//        Files.setAttribute(file.toPath(), "user:description", ByteBuffer.wrap(DESCRIPTION_VALUE.getBytes()));
    }

    @Test
    @SuppressWarnings("deprecated")
    public void uploadFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), MULTIPART_FORM_DATA_VALUE, Files.readAllBytes(file.toPath()));
        assertThat(multipartFile).isNotNull();

        restMvc.perform(multipart("/api/files/upload")
                .file(multipartFile)
                // .param("creationDate", "2017-01-01 18:00:00")
                .accept(MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(TITLE_VALUE))
                .andExpect(jsonPath("$.description").value(DESCRIPTION_VALUE));
    }
}
