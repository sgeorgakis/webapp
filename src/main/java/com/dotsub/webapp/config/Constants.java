package com.dotsub.webapp.config;

public class Constants {

    public enum MetaData {

        CREATION_TIME("creationTime"), TITLE("title"), DESCRIPTION("description");

        private String value;

        MetaData(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }
}
