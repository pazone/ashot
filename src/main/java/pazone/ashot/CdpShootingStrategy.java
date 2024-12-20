package pazone.ashot;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.HasCdp;

import pazone.ashot.coordinates.Coords;
import pazone.ashot.util.ImageTool;

/**
 * Gets a screenshot using
 * <a href="https://chromedevtools.github.io/devtools-protocol/tot/Page/#method-captureScreenshot">
 * capture screenshot</a> function provided by Chrome DevTools protocol. {@link WebDriver} instance provided
 * to the class methods must be an instance of {@link HasCdp} and support Chrome DevTools protocol.
 */
public class CdpShootingStrategy implements ShootingStrategy {

    private static final long serialVersionUID = -4371668803381640029L;

    @Override
    public BufferedImage getScreenshot(WebDriver driver) {
        return getScreenshot(driver, Set.of());
    }

    @Override
    public BufferedImage getScreenshot(WebDriver driver, Set<Coords> coords) {
        if (!HasCdp.class.isAssignableFrom(driver.getClass())) {
            throw new IllegalArgumentException("WebDriver instance must support Chrome DevTools protocol");
        }

        Map<String, Object> args = new HashMap<>();
        args.put("captureBeyondViewport", true);

        if (!coords.isEmpty()) {
            Coords elementCoords = coords.iterator().next();
            args.put("clip", Map.of(
                "x", elementCoords.x,
                "y", elementCoords.y,
                "width", elementCoords.width,
                "height", elementCoords.height,
                "scale", 1)
            );
        }

        Map<String, Object> results = ((HasCdp) driver).executeCdpCommand("Page.captureScreenshot", args);
        String base64 = (String) results.get("data");
        byte[] bytes = OutputType.BYTES.convertFromBase64Png(base64);

        try {
            return ImageTool.toBufferedImage(bytes);
        } catch (IOException thrown) {
            throw new UncheckedIOException(thrown);
        }
    }

    @Override
    public Set<Coords> prepareCoords(Set<Coords> coordsSet) {
        return coordsSet;
    }
}
