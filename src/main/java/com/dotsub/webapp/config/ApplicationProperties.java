package com.dotsub.webapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Webapp.
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Upload upload = new Upload();

    public Upload getUpload() {
        return upload;
    }

    public static class Upload {

        private String saveFolder = "./";

        public String getSaveFolder() {
            return saveFolder;
        }

        public void setSaveFolder(String saveFolder) {
            this.saveFolder = saveFolder;
        }
    }

}
