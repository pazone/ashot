package ru.yandex.qatools.elementscompare.tests;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ru.yandex.qatools.ashot.util.ImageBytesDiffer.areImagesEqual;
import static ru.yandex.qatools.elementscompare.tests.DifferTest.loadImage;

/**
 * @author <a href="frolic@yandex-team.ru">Viacheslav Frolov</a>
 */
public class ImageBytesDifferTest {

    @Test
    public void testImageDifferentSizes() {
        assertFalse("Images should differ",
                areImagesEqual(loadImage("img/SolidColor.png"), loadImage("img/SolidColor_scaled.png")));
    }

    @Test
    public void testOnePixelDifference() {
        assertFalse("Images should differ",
                areImagesEqual(loadImage("img/SolidColor.png"), loadImage("img/SolidColor_1px_red.png")));
    }

    @Test
    public void testColorModeDifference() {
        assertFalse("Images should differ",
                areImagesEqual(loadImage("img/SolidColor.png"), loadImage("img/SolidColor_indexed.png")));
    }

    @Test
    public void testEqualImages() {
        assertTrue("Images should equal",
                areImagesEqual(loadImage("img/SolidColor.png"), loadImage("img/SolidColor.png")));
    }
}
