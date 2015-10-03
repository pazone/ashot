package ru.yandex.qatools.elementscompare.tests;

import org.junit.Test;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.Coords;
import ru.yandex.qatools.ashot.cropper.DefaultCropper;
import ru.yandex.qatools.ashot.cropper.indent.IndentCropper;
import ru.yandex.qatools.ashot.cropper.indent.IndentFilerFactory;
import ru.yandex.qatools.ashot.util.ImageTool;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.elementscompare.tests.DifferTest.IMAGE_A_SMALL;
import static ru.yandex.qatools.elementscompare.tests.DifferTest.loadImage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class CroppersTest {

    private static final Set<Coords> OUTSIDE_IMAGE = Collections.singleton(new Coords(20, 20, 200, 90));
    private static final Set<Coords> INSIDE_IMAGE = Collections.singleton(new Coords(20, 20, 30, 30));


    @Test
    public void testElementOutsideImageDefCropper() throws Exception {
        Screenshot screenshot = new DefaultCropper().cropScreenshot(IMAGE_A_SMALL, OUTSIDE_IMAGE);
        assertThat(screenshot.getImage(), ImageTool.equalImage(loadImage("img/expected/outside_dc.png")));
    }

    @Test
    public void testElementOutsideImageIndentCropper() throws Exception {
        Screenshot screenshot = new IndentCropper(10).cropScreenshot(IMAGE_A_SMALL, OUTSIDE_IMAGE);
        assertThat(screenshot.getImage(), ImageTool.equalImage(loadImage("img/expected/outside_ic.png")));
    }

    @Test
    public void testElementInsideImageIndentCropperWithFilter() throws Exception {
        Screenshot screenshot = new IndentCropper()
            .addIndentFilter(IndentFilerFactory.blur())
            .addIndentFilter(IndentFilerFactory.monochrome())
            .cropScreenshot(IMAGE_A_SMALL, INSIDE_IMAGE);
        assertThat(screenshot.getImage(), ImageTool.equalImage(loadImage("img/expected/inside_icf.png")));
    }

}
