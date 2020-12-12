package pazone.ashot.coordinates;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */
public class WebDriverCoordsProvider extends CoordsProvider {
    @Override
    public Coords ofElement(WebDriver driver, WebElement element) {
        // Keep Point/Dimension as opposed to Rectangle because browsers like PhantomJS do not like the latter
        Point point = element.getLocation();
        Dimension dimension = element.getSize();
        return new Coords(
                point.getX(),
                point.getY(),
                dimension.getWidth(),
                dimension.getHeight());
    }
}
