package pazone.ashot;

import org.openqa.selenium.WebDriver;
import pazone.ashot.coordinates.Coords;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Set;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */
public interface ShootingStrategy extends Serializable {

    /**
     * Get's screenshot of whole page or viewport (depends on browser)
     *
     * @param wd WebDrvier
     * @return image of the whole page or viewport
     */
    BufferedImage getScreenshot(WebDriver wd);

    /**
     * Get's screenshot of area or areas that are defined by {@link Coords}
     *
     * @param wd WebDriver
     * @param coords Set of coordinates to shoot
     * @return minimal image with required coords
     */
    BufferedImage getScreenshot(WebDriver wd, Set<Coords> coords);

    /**
     * Prepares coordinated for cropper and ignored areas
     *
     * @param coordsSet to prepare
     * @return New set of prepared coordinates
     */
    Set<Coords> prepareCoords(Set<Coords> coordsSet);

}
