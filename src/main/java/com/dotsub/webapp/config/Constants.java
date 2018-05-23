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

    public enum ErrorCode {

        FILE_ALREADY_EXISTS_ERROR(1, "File name already exists. Rename the file and try again."),
        NO_HANDLER_FOUND_ERROR(2, "The requested endpoint cannot be found"),
        INVALID_ARGUMENTS_ERROR(3, "The request's parameters were not valid"),
        IO_ERROR_ERROR(4, "There was an error while handling the file"),
        INTERNAL_SERVER_ERROR(5, "Something went wrong.");


        private int code;

        private String message;

        ErrorCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return this.code;
        }

        public String getMessage() {
            return this.message;
        }
    }
}
