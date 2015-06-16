package ru.yandex.qatools.elementscompare.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.DiffMarkupPolicy;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.comparison.ImageMarkupPolicy;
import ru.yandex.qatools.ashot.comparison.PointsMarkupPolicy;
import ru.yandex.qatools.ashot.coordinates.Coords;
import ru.yandex.qatools.ashot.util.ImageTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

@RunWith(Parameterized.class)
public class DifferTest {

    public static final BufferedImage IMAGE_A_SMALL = loadImage("img/A_s.png");
    public static final BufferedImage IMAGE_B_SMALL = loadImage("img/B_s.png");
    public static final BufferedImage IMAGE_B_BIG = loadImage("img/B_b.png");
    public ImageDiffer imageDiffer;

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[][] {
                {PointsMarkupPolicy.class},
                {ImageMarkupPolicy.class}
        });
    }

    @Parameterized.Parameter
    public Class<? extends DiffMarkupPolicy> diffStorageClass;

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        imageDiffer = new ImageDiffer()
                .withColorDistortion(10)
                .withDiffMarkupPolicy(diffStorageClass.newInstance()
                        .withDiffColor(Color.RED));
    }

    @Test
    public void testSameSizeDiff() throws Exception {
        ImageDiff diff = imageDiffer.makeDiff(IMAGE_A_SMALL, IMAGE_B_SMALL);
        assertThat(diff.getMarkedImage(), ImageTool.equalImage(loadImage("img/expected/same_size_diff.png")));
    }

    @Test
    public void testDifferentSizeDiff() throws Exception {
        ImageDiff diff = imageDiffer.makeDiff(IMAGE_B_SMALL, IMAGE_B_BIG);
        assertThat(diff.getMarkedImage(), ImageTool.equalImage(loadImage("img/expected/different_size_diff.png")));
    }

    @Test
    public void testSetDiffColor() throws Exception {
        ImageDiffer greenDiffer = new ImageDiffer()
                .withDiffMarkupPolicy(
                        new ImageMarkupPolicy()
                                .withDiffColor(Color.GREEN)
                );
        ImageDiff diff = greenDiffer.makeDiff(IMAGE_A_SMALL, IMAGE_B_SMALL);
        assertThat(diff.getMarkedImage(), ImageTool.equalImage(loadImage("img/expected/green_diff.png")));
    }

    @Test
    public void testSetDiffSizeTrigger() throws Exception {
        ImageDiff diff = imageDiffer.makeDiff(IMAGE_A_SMALL, IMAGE_B_SMALL);
        assertThat(diff.withDiffSizeTrigger(624).hasDiff(), is(false));
        assertThat(diff.withDiffSizeTrigger(623).hasDiff(), is(true));
    }

    @Test
    public void testEqualImagesDiff() throws Exception {
        ImageDiff diff = imageDiffer.makeDiff(IMAGE_A_SMALL, IMAGE_A_SMALL);
        assertFalse(diff.hasDiff());
    }

    @Test
    public void testIgnoredCoordsSame() throws Exception {
        Screenshot a = createScreenshotWithSameIgnoredAreas(IMAGE_A_SMALL);
        Screenshot b = createScreenshotWithSameIgnoredAreas(IMAGE_B_SMALL);
        ImageDiff diff = imageDiffer.makeDiff(a, b);
        assertThat(diff.getMarkedImage(), ImageTool.equalImage(loadImage("img/expected/ignore_coords_same.png")));
    }

    @Test
    public void testIgnoredCoordsNotSame() throws Exception {
        Screenshot a = createScreenshotWithIgnoredAreas(IMAGE_A_SMALL, new HashSet<>(asList(new Coords(55, 55))));
        Screenshot b = createScreenshotWithIgnoredAreas(IMAGE_B_SMALL, new HashSet<>(asList(new Coords(80, 80))));
        ImageDiff diff = imageDiffer.makeDiff(a, b);
        assertThat(diff.getMarkedImage(), ImageTool.equalImage(loadImage("img/expected/ignore_coords_not_same.png")));
    }

    @Test
    public void testCoordsToCompareAndIgnoredCombine() throws Exception {
        Screenshot a = createScreenshotWithIgnoredAreas(IMAGE_A_SMALL, new HashSet<>(asList(new Coords(60, 60))));
        a.setCoordsToCompare(new HashSet<>(asList(new Coords(50, 50, 100, 100))));
        Screenshot b = createScreenshotWithIgnoredAreas(IMAGE_B_SMALL, new HashSet<>(asList(new Coords(80, 80))));
        b.setCoordsToCompare(new HashSet<>(asList(new Coords(50, 50, 100, 100))));
        ImageDiff diff = imageDiffer.makeDiff(a, b);
        assertThat(diff.getMarkedImage(), ImageTool.equalImage(loadImage("img/expected/combined_diff.png")));
    }

    @Test
    public void testTransparentImageWithMarkedDiff() {
        ImageDiff imageDiff = imageDiffer.makeDiff(loadImage("img/A_s.png"), loadImage("img/B_s.png"));
        assertThat(imageDiff.getTransparentMarkedImage(), ImageTool.equalImage(loadImage("img/expected/transparent_diff.png")));
    }

    @Test
    public void testDiffSize() {
        String path = "img/A_s.png";
        BufferedImage image1 = loadImage(path);
        BufferedImage image2 = loadImage(path);

        int rgb = Color.GREEN.getRGB();
        int diffSize = 10;
        for (int i = 0; i < diffSize; i++) {
            image2.setRGB(i, 1, rgb);
        }

        ImageDiff imageDiff = imageDiffer.makeDiff(image1, image2);
        assertEquals("Should have diff size " + diffSize, diffSize, imageDiff.getDiffSize());
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
