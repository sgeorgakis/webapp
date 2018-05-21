package com.dotsub.webapp.config;

public class Constants {

    public enum MetaData {

        CREATION_TIME("creationTime"), TITLE("title"), DESCRIPTION("description");

        private String value;
        private static String metadataValues;

        MetaData(String value) {
            this.value = value;
        }

        static {
            metadataValues = CREATION_TIME.getValue();
//            StringBuffer buffer = new StringBuffer();
//            for (MetaData metaData : MetaData.values()) {
//                buffer.append(metaData.getValue());
//                buffer.append(",");
//            }
//            if (buffer.length() > 0) {
//                metadataValues = buffer.substring(0, buffer.length() - 1);
//            } else {
//                metadataValues = "";
//            }
        }

        public String getValue() {
            return value;
        }

        public static String getMetadata() {
            return metadataValues;
        }
    }
}
