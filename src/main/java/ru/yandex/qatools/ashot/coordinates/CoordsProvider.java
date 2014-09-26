package ru.yandex.qatools.ashot.coordinates;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public abstract class CoordsProvider {

    public abstract Coords ofElement(WebDriver driver, WebElement element);

    public Coords ofElements(WebDriver driver, Iterable<WebElement> elements) {
        Iterator<WebElement> iterator = elements.iterator();
        if (!iterator.hasNext()) return null;

        Coords coords = ofElement(driver, iterator.next());
        while (iterator.hasNext()) {
            coords = new Coords(coords.union(ofElement(driver, iterator.next())));
        }

        return coords;
    }

    public Coords ofElements(WebDriver driver, WebElement... elements) {
        return ofElements(driver, Arrays.asList(elements));
    }

    public Coords locatedBy(WebDriver driver, By locator) {
        return ofElements(driver, driver.findElements(locator));
    }

}
