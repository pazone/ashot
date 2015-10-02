package ru.yandex.qatools.ashot.shooting;

import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.coordinates.Coords;

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
     * @return
     */
    BufferedImage getScreenshot(WebDriver wd);

    /**
     * Get's screenshot of area or areas that are defined by {@link Coords}
     *
     * @param wd WebDriver
     * @param coords Set of coordinates to shoot
     * @return
     */
    BufferedImage getScreenshot(WebDriver wd, Set<Coords> coords);

}
