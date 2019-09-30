package pazone.ashot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import pazone.ashot.cutter.FixedCutStrategy;
import pazone.ashot.util.ImageTool;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.mockito.Mockito.when;
import static pazone.ashot.util.TestImageUtils.IMAGE_A_SMALL;
import static pazone.ashot.util.TestImageUtils.assertImageEquals;

/**
 * @author <a href="rovner@yandex-team.ru">Rovniakov Viacheslav</a>
 */
@ExtendWith(MockitoExtension.class)
class RotatingDecoratorTest {

    @Mock(extraInterfaces = TakesScreenshot.class)
    private WebDriver webDriver;

    @Test
    void testRotating() throws IOException {
        when(((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES)).thenReturn(
                ImageTool.toByteArray(IMAGE_A_SMALL));
        ShootingStrategy strategy = new RotatingDecorator(new FixedCutStrategy(0, 0), new SimpleShootingStrategy());
        BufferedImage screenshot = strategy.getScreenshot(webDriver);
        assertImageEquals(screenshot, "img/expected/rotated.png");
    }
}
