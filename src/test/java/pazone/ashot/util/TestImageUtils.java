package pazone.ashot.util;

import static org.hamcrest.MatcherAssert.assertThat;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class TestImageUtils {

    public static final String IMAGE_A_SMALL_PATH = "img/A_s.png";
    public static final BufferedImage IMAGE_A_SMALL = loadImage(IMAGE_A_SMALL_PATH);
    public static final BufferedImage IMAGE_B_SMALL = loadImage("img/B_s.png");

    private TestImageUtils() {
    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void assertImageEquals(BufferedImage actualImage, String expectedImagePath) {
        assertImageEquals(actualImage, loadImage(expectedImagePath));
    }

    public static void assertImageEquals(BufferedImage actualImage, BufferedImage expectedImage) {
        assertThat(actualImage, ImageTool.equalImage(expectedImage));
    }
}
