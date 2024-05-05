package com.ikarabulut.shareable.common;

public enum AllowedFileTypes {
    TIF(".tif"),
    PNG(".png");

    private final String extension;
    AllowedFileTypes(String extension){
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
    public static boolean isFileTypeAllowed(String fileType) {
        for (AllowedFileTypes allowedType : AllowedFileTypes.values()) {
            if (fileType.equals(allowedType.getExtension())) {
                return true;
            }
        }
        return false;
    }


}
