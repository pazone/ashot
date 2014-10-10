package ru.yandex.qatools.ashot.coordinates;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public abstract class CoordsProvider {

    public abstract Coords ofElement(WebDriver driver, WebElement element);

    public Set<Coords> ofElements(WebDriver driver, Iterable<WebElement> elements) {
        Set<Coords> elementsCoords = new HashSet<>();
        for (WebElement element : elements) {
            Coords elementCoords = ofElement(driver, element);
            if (!elementCoords.isEmpty()) {
                elementsCoords.add(ofElement(driver, element));
            }
        }
        return elementsCoords;
    }

    @SuppressWarnings("UnusedDeclaration")
    public Set<Coords> ofElements(WebDriver driver, WebElement... elements) {
        return ofElements(driver, Arrays.asList(elements));
    }

    @SuppressWarnings("UnusedDeclaration")
    public Set<Coords> locatedBy(WebDriver driver, By locator) {
        return ofElements(driver, driver.findElements(locator));
    }

}
