package ru.yandex.qatools.ashot.coordinates;

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
        Point point = element.getLocation();
        Dimension dimension = element.getSize();
        return new Coords(
                point.getX(),
                point.getY(),
                dimension.getWidth(),
                dimension.getHeight());
    }
}
