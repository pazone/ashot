package pazone.ashot;

import pazone.ashot.comparison.DiffMarkupPolicy;
import pazone.ashot.comparison.ImageDiff;
import pazone.ashot.comparison.ImageDiffer;
import pazone.ashot.comparison.ImageMarkupPolicy;
import pazone.ashot.comparison.PointsMarkupPolicy;
import pazone.ashot.coordinates.Coords;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static pazone.ashot.util.TestImageUtils.IMAGE_A_SMALL;
import static pazone.ashot.util.TestImageUtils.IMAGE_A_SMALL_PATH;
import static pazone.ashot.util.TestImageUtils.IMAGE_B_SMALL;
import static pazone.ashot.util.TestImageUtils.assertImageEquals;
import static pazone.ashot.util.TestImageUtils.loadImage;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */
class DifferTest {

    private static final BufferedImage IMAGE_B_BIG = loadImage("img/B_b.png");
    private static final String IMAGE_IGNORED_TEMPLATE = "img/ignore_color_template.png";
    private static final String IMAGE_IGNORED_PASS = "img/ignore_color_pass.png";
    private static final String IMAGE_IGNORED_FAIL = "img/ignore_color_fail.png";

    private static Stream<DiffMarkupPolicy> data() {
        return Stream.of(new PointsMarkupPolicy(), new ImageMarkupPolicy());
    }

    private ImageDiffer createImageDiffer(DiffMarkupPolicy diffMarkupPolicy) {
        return new ImageDiffer()
                .withColorDistortion(10)
                .withDiffMarkupPolicy(diffMarkupPolicy.withDiffColor(Color.RED));
    }

    @ParameterizedTest
    @MethodSource("data")
    void testSameSizeDiff(DiffMarkupPolicy diffMarkupPolicy) {
        ImageDiff diff = createImageDiffer(diffMarkupPolicy).makeDiff(IMAGE_A_SMALL, IMAGE_B_SMALL);

        assertAll(
                () -> assertImageEquals(diff.getMarkedImage(), "img/expected/same_size_diff.png"),
                () -> assertImageEquals(diff.getTransparentMarkedImage(), "img/expected/transparent_diff.png"),
                () -> assertThat(diff.withDiffSizeTrigger(624).hasDiff(), is(false)),
                () -> assertThat(diff.withDiffSizeTrigger(623).hasDiff(), is(true))
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    void testDifferentSizeDiff(DiffMarkupPolicy diffMarkupPolicy) {
        ImageDiff diff = createImageDiffer(diffMarkupPolicy).makeDiff(IMAGE_B_SMALL, IMAGE_B_BIG);
        assertImageEquals(diff.getMarkedImage(), "img/expected/different_size_diff.png");
    }

    @ParameterizedTest
    @MethodSource("data")
    void testSetDiffColor(DiffMarkupPolicy diffMarkupPolicy) {
        ImageDiffer greenDiffer = new ImageDiffer()
                .withDiffMarkupPolicy(diffMarkupPolicy.withDiffColor(Color.GREEN));
        ImageDiff diff = greenDiffer.makeDiff(IMAGE_A_SMALL, IMAGE_B_SMALL);
        assertImageEquals(diff.getMarkedImage(), "img/expected/green_diff.png");
    }

    @ParameterizedTest
    @MethodSource("data")
    void testEqualImagesDiff(DiffMarkupPolicy diffMarkupPolicy) {
        ImageDiff diff = createImageDiffer(diffMarkupPolicy).makeDiff(IMAGE_A_SMALL, IMAGE_A_SMALL);
        assertFalse(diff.hasDiff());
    }

    static Stream<Arguments> dataWithIgnoredColorDiff() {
        return Stream.of(
                Arguments.of(Color.MAGENTA, IMAGE_IGNORED_PASS, false),
                Arguments.of(Color.MAGENTA, IMAGE_IGNORED_FAIL, true),
                Arguments.of(Color.RED,     IMAGE_IGNORED_PASS, true)
        );
    }

    @ParameterizedTest
    @MethodSource("dataWithIgnoredColorDiff")
    void testDiffImagesWithIgnoredColorDiff(Color ignoredColor, String imageToCompare, boolean hasDiff) {
        ImageDiffer imageDifferWithIgnored = new ImageDiffer().withIgnoredColor(ignoredColor);
        ImageDiff diff = imageDifferWithIgnored.makeDiff(loadImage(IMAGE_IGNORED_TEMPLATE), loadImage(imageToCompare));
        assertThat(diff.hasDiff(), equalTo(hasDiff));
    }

    @ParameterizedTest
    @MethodSource("data")
    void testIgnoredCoordsSame(DiffMarkupPolicy diffMarkupPolicy) {
        Screenshot a = createScreenshotWithIgnoredAreas(IMAGE_A_SMALL, new Coords(50, 50));
        Screenshot b = createScreenshotWithIgnoredAreas(IMAGE_B_SMALL, new Coords(50, 50));
        ImageDiff diff = createImageDiffer(diffMarkupPolicy).makeDiff(a, b);
        assertImageEquals(diff.getMarkedImage(), "img/expected/ignore_coords_same.png");
    }

    @ParameterizedTest
    @MethodSource("data")
    void testIgnoredCoordsNotSame(DiffMarkupPolicy diffMarkupPolicy) {
        Screenshot a = createScreenshotWithIgnoredAreas(IMAGE_A_SMALL, new Coords(55, 55));
        Screenshot b = createScreenshotWithIgnoredAreas(IMAGE_B_SMALL, new Coords(80, 80));
        ImageDiff diff = createImageDiffer(diffMarkupPolicy).makeDiff(a, b);
        assertImageEquals(diff.getMarkedImage(), "img/expected/ignore_coords_not_same.png");
    }

    @ParameterizedTest
    @MethodSource("data")
    void testCoordsToCompareAndIgnoredCombine(DiffMarkupPolicy diffMarkupPolicy) {
        Screenshot a = createScreenshotWithIgnoredAreas(IMAGE_A_SMALL, new Coords(60, 60));
        a.setCoordsToCompare(Collections.singleton(new Coords(50, 50, 100, 100)));
        Screenshot b = createScreenshotWithIgnoredAreas(IMAGE_B_SMALL, new Coords(80, 80));
        b.setCoordsToCompare(Collections.singleton(new Coords(50, 50, 100, 100)));
        ImageDiff diff = createImageDiffer(diffMarkupPolicy).makeDiff(a, b);
        assertImageEquals(diff.getMarkedImage(), "img/expected/combined_diff.png");
    }

    @ParameterizedTest
    @MethodSource("data")
    void testDiffSize(DiffMarkupPolicy diffMarkupPolicy) {
        String path = IMAGE_A_SMALL_PATH;
        BufferedImage image1 = loadImage(path);
        BufferedImage image2 = loadImage(path);

        int rgb = Color.GREEN.getRGB();
        int diffSize = 10;
        for (int i = 0; i < diffSize; i++) {
            image2.setRGB(i, 1, rgb);
        }

        ImageDiff imageDiff = createImageDiffer(diffMarkupPolicy).makeDiff(image1, image2);
        assertEquals(diffSize, imageDiff.getDiffSize(), "Should have diff size " + diffSize);
    }

    private Screenshot createScreenshotWithIgnoredAreas(BufferedImage image, Coords ignoredArea) {
        Screenshot screenshot = new Screenshot(image);
        screenshot.setIgnoredAreas(Collections.singleton(ignoredArea));
        return screenshot;
    }
}
