package ru.yandex.qatools.elementscompare.tests;

import org.junit.Test;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.Coords;
import ru.yandex.qatools.ashot.util.ImageTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class DifferTest {

    public static final BufferedImage IMAGE_A_SMALL = loadImage("img/A_s.png");
    public static final BufferedImage IMAGE_B_SMALL = loadImage("img/B_s.png");

    static BufferedImage loadImage(String path) {
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
        List<Coords> ignored = new ArrayList<>();
        ignored.add(new Coords(50, 50));
        ImageDiffer differ = new ImageDiffer().withIgnoredCoords(ignored, ignored);
        ImageDiff diff = differ.makeDiff(IMAGE_A_SMALL, IMAGE_B_SMALL);
        assertThat(diff.getMarkedImage(), ImageTool.equalImage(loadImage("img/expected/ignore_coords_same.png")));
    }

    @Test
    public void testIgnoredCoordsNotSame() throws Exception {
        List<Coords> ignoredExpected = new ArrayList<>();
        List<Coords> ignoredActual = new ArrayList<>();

        ignoredExpected.add(new Coords(50, 50));
        ignoredActual.add(new Coords(80, 80));

        ImageDiffer differ = new ImageDiffer()
                .withIgnoredCoords(ignoredExpected, ignoredActual);
        ImageDiff diff = differ.makeDiff(IMAGE_A_SMALL, IMAGE_B_SMALL);
        assertThat(diff.getMarkedImage(), ImageTool.equalImage(loadImage("img/expected/ignore_coords_not_same.png")));
    }
}
