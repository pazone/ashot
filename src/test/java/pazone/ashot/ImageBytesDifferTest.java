package pazone.ashot;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static pazone.ashot.util.ImageBytesDiffer.areImagesEqual;
import static pazone.ashot.util.TestImageUtils.loadImage;

import java.util.stream.Stream;

/**
 * @author <a href="frolic@yandex-team.ru">Viacheslav Frolov</a>
 */
class ImageBytesDifferTest {

    @TestFactory
    Stream<DynamicTest> testDifferentImages() {
        return Stream.of(
                dynamicTest("Different size", () -> testDifferentImages("img/SolidColor_scaled.png")),
                dynamicTest("One pixel difference", () -> testDifferentImages("img/SolidColor_1px_red.png")),
                dynamicTest("Color mode difference", () -> testDifferentImages("img/SolidColor_indexed.png"))
        );
    }

    void testDifferentImages(String path) {
        assertFalse(areImagesEqual(loadImage("img/SolidColor.png"), loadImage(path)), "Images should differ");
    }

    @Test
    void testEqualImages() {
        assertTrue(areImagesEqual(loadImage("img/SolidColor.png"), loadImage("img/SolidColor.png")),
                "Images should equal");
    }
}
