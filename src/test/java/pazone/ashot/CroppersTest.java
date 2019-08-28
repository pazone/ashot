package pazone.ashot;

import org.junit.Test;
import pazone.ashot.coordinates.Coords;
import pazone.ashot.cropper.DefaultCropper;
import pazone.ashot.cropper.indent.IndentCropper;
import pazone.ashot.cropper.indent.IndentFilerFactory;
import pazone.ashot.util.ImageTool;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertThat;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class CroppersTest {

    private static final Set<Coords> OUTSIDE_IMAGE = Collections.singleton(new Coords(20, 20, 200, 90));
    private static final Set<Coords> INSIDE_IMAGE = Collections.singleton(new Coords(20, 20, 30, 30));


    @Test
    public void testElementOutsideImageDefCropper() throws Exception {
        Screenshot screenshot = new DefaultCropper().cropScreenshot(DifferTest.IMAGE_A_SMALL, OUTSIDE_IMAGE);
        assertThat(screenshot.getImage(), ImageTool.equalImage(DifferTest.loadImage("img/expected/outside_dc.png")));
    }

    @Test
    public void testElementOutsideImageIndentCropper() throws Exception {
        Screenshot screenshot = new IndentCropper(10).cropScreenshot(DifferTest.IMAGE_A_SMALL, OUTSIDE_IMAGE);
        assertThat(screenshot.getImage(), ImageTool.equalImage(DifferTest.loadImage("img/expected/outside_ic.png")));
    }

    @Test
    public void testElementInsideImageIndentCropperWithFilter() throws Exception {
        Screenshot screenshot = new IndentCropper()
            .addIndentFilter(IndentFilerFactory.blur())
            .addIndentFilter(IndentFilerFactory.monochrome())
            .cropScreenshot(DifferTest.IMAGE_A_SMALL, INSIDE_IMAGE);
        assertThat(screenshot.getImage(), ImageTool.equalImage(DifferTest.loadImage("img/expected/inside_icf.png")));
    }

}
