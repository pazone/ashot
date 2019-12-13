package pazone.ashot.coordinates;

import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */
public class WebDriverCoordsProvider extends CoordsProvider {
    @Override
    public Coords ofElement(WebDriver driver, WebElement element) {
        Rectangle rect = element.getRect();
        return new Coords(
                rect.getX(),
                rect.getY(),
                rect.getWidth(),
                rect.getHeight());
    }
}
