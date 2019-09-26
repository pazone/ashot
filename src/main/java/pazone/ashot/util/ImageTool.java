package pazone.ashot.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import pazone.ashot.Screenshot;
import pazone.ashot.coordinates.Coords;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public final class ImageTool {

    private ImageTool() {
        throw new UnsupportedOperationException();
    }

    public static BufferedImage subImage(BufferedImage origin, Coords crop) {
        Coords intersection = Coords.ofImage(origin).intersection(crop);
        return origin.getSubimage(intersection.x, intersection.y, intersection.width, intersection.height);
    }

    public static Coords spreadCoordsInsideImage(Coords coordinates, int indent, BufferedImage image) {
        return new Coords(Math.max(0, coordinates.x - indent),
                Math.max(0, coordinates.y - indent),
                Math.min(image.getWidth(), coordinates.width + indent),
                Math.min(image.getHeight(), coordinates.height + indent));

    }

    public static boolean rgbCompare(int rgb1, int rgb2, int inaccuracy) {
        if (inaccuracy == 0) return rgb1 == rgb2;
        int red1 = (rgb1 & 0x00FF0000) >> 16;
        int green1 = (rgb1 & 0x0000FF00) >> 8;
        int blue1 = (rgb1 & 0x000000FF);
        int red2 = (rgb2 & 0x00FF0000) >> 16;
        int green2 = (rgb2 & 0x0000FF00) >> 8;
        int blue2 = (rgb2 & 0x000000FF);
        return Math.abs(red1 - red2) <= inaccuracy &&
                Math.abs(green1 - green2) <= inaccuracy &&
                Math.abs(blue1 - blue2) <= inaccuracy;
    }

    public static Matcher<BufferedImage> equalImage(final BufferedImage second) {
        return new TypeSafeMatcher<BufferedImage>() {

            @Override
            protected boolean matchesSafely(BufferedImage first) {
                if (!Coords.ofImage(first).equals(Coords.ofImage(second))) {
                    return false;
                }
                for (int x = 0; x < first.getWidth(); x++) {
                    for (int y = 0; y < first.getHeight(); y++) {
                        if (!rgbCompare(first.getRGB(x, y), second.getRGB(x, y), 10)) {
                            return false;
                        }
                    }
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    @SuppressWarnings("UnusedDeclaration")
    public static byte[] toByteArray(Screenshot screenshot) throws IOException {
        return toByteArray(screenshot.getImage());
    }

    public static byte[] toByteArray(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        }
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bufferedImage = new BufferedImage(
            img.getWidth(null),
            img.getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();
        return bufferedImage;
    }

}
