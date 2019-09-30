package pazone.ashot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pazone.ashot.coordinates.Coords;
import pazone.ashot.cropper.DefaultCropper;
import pazone.ashot.cropper.ImageCropper;
import pazone.ashot.cropper.indent.IndentCropper;
import pazone.ashot.cropper.indent.IndentFilerFactory;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static pazone.ashot.util.TestImageUtils.IMAGE_A_SMALL;
import static pazone.ashot.util.TestImageUtils.assertImageEquals;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */
class CroppersTest {

    private static final Set<Coords> OUTSIDE_IMAGE = Collections.singleton(new Coords(20, 20, 200, 90));
    private static final Set<Coords> INSIDE_IMAGE = Collections.singleton(new Coords(20, 20, 30, 30));

    static Stream<Arguments> outsideCropperData() {
        return Stream.of(
                Arguments.of(new DefaultCropper(), "img/expected/outside_dc.png"),
                Arguments.of(new IndentCropper(10), "img/expected/outside_ic.png")
        );
    }

    @ParameterizedTest
    @MethodSource("outsideCropperData")
    void testElementOutsideImageCropper(ImageCropper cropper, String expectedImagePath) {
        Screenshot screenshot = cropper.crop(IMAGE_A_SMALL, OUTSIDE_IMAGE);
        assertImageEquals(screenshot.getImage(), expectedImagePath);
    }

    @Test
    void testElementInsideImageIndentCropperWithFilter() {
        Screenshot screenshot = new IndentCropper()
            .addIndentFilter(IndentFilerFactory.blur())
            .addIndentFilter(IndentFilerFactory.monochrome())
            .cropScreenshot(IMAGE_A_SMALL, INSIDE_IMAGE);
        assertImageEquals(screenshot.getImage(), "img/expected/inside_icf.png");
    }
}
