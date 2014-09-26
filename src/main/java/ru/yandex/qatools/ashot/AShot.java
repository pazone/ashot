package ru.yandex.qatools.ashot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.coordinates.Coords;
import ru.yandex.qatools.ashot.coordinates.CoordsPreparationStrategy;
import ru.yandex.qatools.ashot.coordinates.CoordsProvider;
import ru.yandex.qatools.ashot.coordinates.JqueryCoordsProvider;
import ru.yandex.qatools.ashot.cropper.DefaultCropper;
import ru.yandex.qatools.ashot.cropper.ImageCropper;
import ru.yandex.qatools.ashot.screentaker.ScreenTaker;
import ru.yandex.qatools.ashot.screentaker.ShootingStrategy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ru.yandex.qatools.ashot.coordinates.CoordsPreparationStrategy.intersectingWith;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class AShot {

    private ScreenTaker taker = new ScreenTaker();
    private CoordsProvider coordsProvider = new JqueryCoordsProvider();
    private ImageCropper cropper = new DefaultCropper();
    private List<By> ignoredLocators = new ArrayList<>();

    public AShot coordsProvider(final CoordsProvider coordsProvider) {
        this.coordsProvider = coordsProvider;
        return this;
    }

    public AShot imageCropper(ImageCropper cropper) {
        this.cropper = cropper;
        return this;
    }

    /**
     * Sets taker impl.
     * Usually is not used.
     *
     * @param taker ScreenTaker
     * @return this;
     * @see ru.yandex.qatools.ashot.screentaker.ScreenTaker
     */
    public AShot screenTaker(ScreenTaker taker) {
        this.taker = taker;
        return this;
    }

    /**
     * Sets the list of locators to ignore during image comparison.
     *
     * @param ignoredElements list of By
     * @return this
     */
    public AShot ignoredElements(final List<By> ignoredElements) {
        this.ignoredLocators = ignoredElements;
        return this;
    }

    /**
     * Adds selector of ignored element.
     *
     * @param selector By
     * @return this
     */
    public AShot addIgnoredElement(final By selector) {
        this.ignoredLocators.add(selector);
        return this;
    }

    /**
     * Sets the policy of taking screenshot.
     *
     * @param strategy shooting strategy
     * @return this
     * @see ru.yandex.qatools.ashot.screentaker.ShootingStrategy
     */
    public AShot shootingStrategy(ShootingStrategy strategy) {
        this.taker.withShootingStrategy(strategy);
        return this;
    }

    /**
     * Sets device pixel ratio.
     * for example, Retina = 2.
     *
     * @param dpr device pixel ratio
     * @return this
     */
    public AShot dpr(float dpr) {
        this.taker.withDpr(dpr);
        return this;
    }

    /**
     * Takes the screenshot of given elements
     * If elements were not found screenshot of whole page will be returned
     *
     * @param driver WebDriver instance
     * @return Screenshot with cropped image and list of ignored areas on screenshot
     * @throws RuntimeException when something goes wrong
     * @see Screenshot
     */
    public Screenshot takeScreenshot(WebDriver driver, Collection<WebElement> elements) {
        Coords elementCoords = coordsProvider.ofElements(driver, elements);
        BufferedImage shot = taker.take(driver);
        Screenshot screenshot = cropper.crop(shot, elementCoords);
        List<Coords> ignoredAreas = compileIgnoredAreas(driver, intersectingWith(elementCoords));
        screenshot.setIgnoredAreas(ignoredAreas);
        return screenshot;
    }

    /**
     * Takes the screenshot of given element
     *
     * @param driver WebDriver instance
     * @return Screenshot with cropped image and list of ignored areas on screenshot
     * @throws RuntimeException when something goes wrong
     * @see Screenshot
     */
    public Screenshot takeScreenshot(WebDriver driver, WebElement element) {
        return takeScreenshot(driver, Arrays.asList(element));
    }

    /**
     * Takes the screenshot of whole page
     *
     * @param driver WebDriver instance
     * @return Screenshot with whole page image and list of ignored areas on screenshot
     * @see Screenshot
     */
    public Screenshot takeScreenshot(WebDriver driver) {
        Screenshot screenshot = new Screenshot(taker.take(driver));
        screenshot.setIgnoredAreas(compileIgnoredAreas(driver, CoordsPreparationStrategy.simple()));
        return screenshot;
    }

    protected List<Coords> compileIgnoredAreas(WebDriver driver, CoordsPreparationStrategy preparationStrategy) {
        List<Coords> ignoredCoords = new ArrayList<>();
        for (By ignoredLocator : ignoredLocators) {
            List<WebElement> ignoredElements = driver.findElements(ignoredLocator);
            if (!ignoredElements.isEmpty()) {
                ignoredCoords.add(preparationStrategy.prepare(coordsProvider.ofElements(driver, ignoredElements)));
            }
        }
        return ignoredCoords;

    }

    public List<By> getIgnoredLocators() {
        return ignoredLocators;
    }
}
