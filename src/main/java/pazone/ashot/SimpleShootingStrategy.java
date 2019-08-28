package pazone.ashot;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import pazone.ashot.coordinates.Coords;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Gets screenshot from webdriver.
 */
public class SimpleShootingStrategy implements ShootingStrategy {

    @Override
    public BufferedImage getScreenshot(WebDriver wd) {
        ByteArrayInputStream imageArrayStream = null;
        TakesScreenshot takesScreenshot;
        try {
            takesScreenshot = (TakesScreenshot) wd;
        } catch (ClassCastException ignored){
            takesScreenshot = (TakesScreenshot) new Augmenter().augment(wd);
        }
        try {
            imageArrayStream = new ByteArrayInputStream(takesScreenshot.getScreenshotAs(OutputType.BYTES));
            return ImageIO.read(imageArrayStream);
        } catch (IOException e) {
            throw new ImageReadException("Can not parse screenshot data", e);
        } finally {
            IOUtils.closeQuietly(imageArrayStream);
        }
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd, Set<Coords> coords) {
        return getScreenshot(wd);
    }

    /**
     * Default behavior is not to change coords, because by default coordinates are not changed
     */
    @Override
    public Set<Coords> prepareCoords(Set<Coords> coordsSet) {
        return coordsSet;
    }
}
