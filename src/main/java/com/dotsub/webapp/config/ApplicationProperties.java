package com.dotsub.webapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Webapp.
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Upload upload = new Upload();

    private final Async async = new Async();

    public Upload getUpload() {
        return upload;
    }

    public Async getAsync() {
        return async;
    }

    public static class Upload {

        private String saveFolder = "./";

        private Boolean deleteFilesOnShutDown = true;

        public String getSaveFolder() {
            return saveFolder;
        }

        public void setSaveFolder(String saveFolder) {
            this.saveFolder = saveFolder;
        }

        public Boolean getDeleteFilesOnShutDown() {
            return deleteFilesOnShutDown;
        }

        public void setDeleteFilesOnShutDown(Boolean deleteFilesOnShutDown) {
            this.deleteFilesOnShutDown = deleteFilesOnShutDown;
        }
    }

    public static class Async {

        private int corePoolSize = 2;
        private int maxPoolSize = 10;
        private int queueCapacity = 100;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

}
