package pazone.ashot.util;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class ImageToolTest {
    @Test
    void shouldConvertImageToBytesAndViceVersa() throws IOException {
        byte[] imageBytes = ImageTool.toByteArray(TestImageUtils.IMAGE_A_SMALL);
        TestImageUtils.assertImageEquals(TestImageUtils.IMAGE_A_SMALL, ImageTool.toBufferedImage(imageBytes));
    }
}
