package pazone.ashot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import pazone.ashot.util.ImageTool;

import static org.mockito.Mockito.when;
import static pazone.ashot.util.TestImageUtils.IMAGE_A_SMALL;
import static pazone.ashot.util.TestImageUtils.assertImageEquals;

import java.awt.image.BufferedImage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */
@ExtendWith(MockitoExtension.class)
class ScalingDecoratorTest {

    @Mock(extraInterfaces = TakesScreenshot.class)
    private WebDriver webDriver;

    @Test
    void testDpr() throws Exception {
        when(((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES)).thenReturn(
                ImageTool.toByteArray(IMAGE_A_SMALL));
        ShootingStrategy dpr2Strategy = new ScalingDecorator(new SimpleShootingStrategy()).withDpr(2);
        BufferedImage screenshot = dpr2Strategy.getScreenshot(webDriver);
        assertImageEquals(screenshot, "img/expected/dpr.png");
    }
}

