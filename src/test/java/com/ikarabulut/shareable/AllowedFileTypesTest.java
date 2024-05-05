package com.ikarabulut.shareable;

import com.ikarabulut.shareable.common.AllowedFileTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class AllowedFileTypesTest {

    @Test
    void isAllowedFile_WithAllowedFile_ReturnsTrue() {
        var validExtension = ".tif";

        assert(AllowedFileTypes.isFileTypeAllowed(validExtension));
    }

    @Test
    void isAllowedFile_WithDisAllowedFile_ReturnsTrue() {
        var validExtension = ".bad";

        assertFalse(AllowedFileTypes.isFileTypeAllowed(validExtension));
    }
}
