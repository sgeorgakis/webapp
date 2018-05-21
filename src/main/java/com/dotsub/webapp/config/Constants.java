package com.dotsub.webapp.config;

public class Constants {

    public enum MetaData {

        TITLE("user:title"), DESCRIPTION("user:description");

        private String value;

        MetaData(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }
}
