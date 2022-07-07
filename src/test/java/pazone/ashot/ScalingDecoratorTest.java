package pazone.ashot;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import pazone.ashot.util.ImageTool;
import pazone.ashot.util.TestImageUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static pazone.ashot.util.TestImageUtils.IMAGE_A_SMALL;
import static pazone.ashot.util.TestImageUtils.assertImageEquals;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */
@ExtendWith(MockitoExtension.class)
class ScalingDecoratorTest {

    @Mock(extraInterfaces = TakesScreenshot.class)
    private WebDriver webDriver;

    @CsvSource({
        "2, img/expected/dpr.png",
        "1, " + TestImageUtils.IMAGE_A_SMALL_PATH
    })
    @ParameterizedTest
    void testDpr(float dpr, String expectedImagePath) throws IOException {
        when(((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES)).thenReturn(
                ImageTool.toByteArray(IMAGE_A_SMALL));
        ShootingStrategy dpr2Strategy = new ScalingDecorator(new SimpleShootingStrategy()).withDpr(dpr);
        BufferedImage screenshot = dpr2Strategy.getScreenshot(webDriver);
        assertImageEquals(screenshot, expectedImagePath);
        assertEquals(IMAGE_A_SMALL.getType(), screenshot.getType());
    }
}

