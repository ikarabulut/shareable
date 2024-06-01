package com.ikarabulut.shareable.file_server.common;

public enum AllowedFileTypes {
    TIF(".tif", "image/tiff"),
    PNG(".png", "image/png"),
    TXT(".txt", "text/plain"),
    NONE(".none", "none/none");

    private final String extension;
    private final String mimeType;

    AllowedFileTypes(String extension, String mimeType){
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }
    public String getMimeType() { return mimeType; }
    public static boolean isFileTypeAllowed(String fileType) {
        for (AllowedFileTypes allowedType : AllowedFileTypes.values()) {
            if (fileType.equals(allowedType.getExtension())) {
                return true;
            }
        }
        return false;
    }

    public static AllowedFileTypes getByExtension(String extension) {
        for (AllowedFileTypes allowedType : AllowedFileTypes.values()) {
            if (extension.equals(allowedType.getExtension())) {
                return allowedType;
            }
        }
        return NONE;
    }


}
