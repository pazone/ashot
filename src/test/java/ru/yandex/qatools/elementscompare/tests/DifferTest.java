package ru.yandex.qatools.elementscompare.tests;

import org.junit.Test;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.Coords;
import ru.yandex.qatools.ashot.util.ImageTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class DifferTest {

    public static final BufferedImage IMAGE_A_SMALL = loadImage("img/A_s.png");
    public static final BufferedImage IMAGE_B_SMALL = loadImage("img/B_s.png");

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSameSizeDiff() throws Exception {
        ImageDiff diff = new ImageDiffer().makeDiff(IMAGE_A_SMALL, IMAGE_B_SMALL);
        assertThat(diff.getMarkedImage(), ImageTool.equalImage(loadImage("img/expected/same_size_diff.png")));
    }

    @Test
    public void testEqualImagesDiff() throws Exception {
        ImageDiff diff = new ImageDiffer().makeDiff(IMAGE_A_SMALL, IMAGE_A_SMALL);
        assertFalse(diff.hasDiff());
    }

    @Test
    public void testIgnoredCoordsSame() throws Exception {
        Screenshot a = createScreenshotWithSameIgnoredAreas(IMAGE_A_SMALL);
        Screenshot b = createScreenshotWithSameIgnoredAreas(IMAGE_B_SMALL);
        ImageDiff diff = new ImageDiffer().makeDiff(a, b);
        assertThat(diff.getMarkedImage(), ImageTool.equalImage(loadImage("img/expected/ignore_coords_same.png")));
    }

    @Test
    public void testIgnoredCoordsNotSame() throws Exception {
        Screenshot a = createScreenshotWithIgnoredAreas(IMAGE_A_SMALL, new HashSet<>(asList(new Coords(50, 50))));
        Screenshot b = createScreenshotWithIgnoredAreas(IMAGE_B_SMALL, new HashSet<>(asList(new Coords(80, 80))));
        ImageDiff diff = new ImageDiffer().makeDiff(a, b);
        assertThat(diff.getMarkedImage(), ImageTool.equalImage(loadImage("img/expected/ignore_coords_not_same.png")));
    }

    @Test
    public void testCoordsToCompareAndIgnoredCombine() throws Exception {
        Screenshot a = createScreenshotWithIgnoredAreas(IMAGE_A_SMALL, new HashSet<>(asList(new Coords(60, 60))));
        a.setCoordsToCompare(new HashSet<>(asList(new Coords(50, 50, 100, 100))));
        Screenshot b = createScreenshotWithIgnoredAreas(IMAGE_B_SMALL, new HashSet<>(asList(new Coords(80, 80))));
        b.setCoordsToCompare(new HashSet<>(asList(new Coords(50, 50, 100, 100))));
        ImageDiff diff = new ImageDiffer().makeDiff(a, b);
        assertThat(diff.getMarkedImage(), ImageTool.equalImage(loadImage("img/expected/combined_diff.png")));
    }

    private Screenshot createScreenshotWithSameIgnoredAreas(BufferedImage image) {
        return createScreenshotWithIgnoredAreas(image, new HashSet<>(asList(new Coords(50, 50))));
    }

    private Screenshot createScreenshotWithIgnoredAreas(BufferedImage image, Set<Coords> ignored) {
        Screenshot screenshot = new Screenshot(image);
        screenshot.setIgnoredAreas(ignored);
        return screenshot;
    }


}
