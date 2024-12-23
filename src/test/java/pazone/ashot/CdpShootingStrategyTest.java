package pazone.ashot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.HasCdp;

import pazone.ashot.coordinates.Coords;
import pazone.ashot.util.ImageTool;
import pazone.ashot.util.TestImageUtils;

@ExtendWith(MockitoExtension.class)
class CdpShootingStrategyTest {
    private final ShootingStrategy strategy = new CdpShootingStrategy();

    @Mock(extraInterfaces = HasCdp.class)
    private WebDriver webDriver;

    @Test
    void testPageScreenshot() throws IOException {
        BufferedImage expected = TestImageUtils.IMAGE_A_SMALL;
        String base = Base64.getEncoder().encodeToString(ImageTool.toByteArray(expected));

        when(((HasCdp) webDriver).executeCdpCommand("Page.captureScreenshot", Map.of("captureBeyondViewport", true)))
                .thenReturn(Map.of("data", base));

        BufferedImage actual = strategy.getScreenshot(webDriver);
        TestImageUtils.assertImageEquals(actual, expected);
    }

    @Test
    void testElementScreenshot() throws IOException {
        BufferedImage expected = TestImageUtils.IMAGE_A_SMALL;
        String base = Base64.getEncoder().encodeToString(ImageTool.toByteArray(expected));
        Coords coords = new Coords(1, 2, 3, 4);

        when(((HasCdp) webDriver).executeCdpCommand("Page.captureScreenshot", Map.of("captureBeyondViewport", true,
                "clip",
                Map.of("x", coords.x, "y", coords.y, "width", coords.width, "height", coords.height, "scale", 1))))
                        .thenReturn(Map.of("data", base));

        BufferedImage actual = strategy.getScreenshot(webDriver, Set.of(coords));
        TestImageUtils.assertImageEquals(actual, expected);
    }

    @Test
    void testUnsupportedCdp() {
        WebDriver driver = mock(WebDriver.class);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> strategy.getScreenshot(driver));
        assertEquals("WebDriver instance must support Chrome DevTools protocol", thrown.getMessage());
    }
}
