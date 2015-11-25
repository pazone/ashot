package ru.yandex.qatools.ashot.shooting;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.qatools.ashot.util.InnerScript.*;

/**
 * Will scroll viewport and shoot to get an image of full page.
 * Useful for browsers on portable devices.
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */
public class ViewportPastingDecorator extends ShootingDecorator {

    protected int scrollTimeout = 0;
    private Coords shootingArea;

    public ViewportPastingDecorator(ShootingStrategy strategy) {
        super(strategy);
    }

    public ViewportPastingDecorator withScrollTimeout(int scrollTimeout) {
        this.scrollTimeout = scrollTimeout;
        return this;
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd) {
        return getScreenshot(wd, null);
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd, Set<Coords> coordsSet) {
        JavascriptExecutor js = (JavascriptExecutor) wd;
        int pageHeight = getFullHeight(wd);
        int pageWidth = getFullWidth(wd);
        int viewportHeight = getWindowHeight(wd);
        shootingArea = getShootingCoords(coordsSet, pageWidth, pageHeight, viewportHeight);

        BufferedImage finalImage = new BufferedImage(pageWidth, shootingArea.height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = finalImage.createGraphics();

        int scrollTimes = (int) Math.ceil(shootingArea.getHeight() / viewportHeight);
        for (int n = 0; n < scrollTimes; n++) {
            scrollVertically(js, shootingArea.y + viewportHeight * n);
            waitForScrolling();
            BufferedImage part = getShootingStrategy().getScreenshot(wd);
            graphics.drawImage(part, 0, getCurrentScrollY(js) - shootingArea.y, null);
        }

        graphics.dispose();
        return finalImage;
    }

    @Override
    public Set<Coords> prepareCoords(Set<Coords> coordsSet) {
        return shootingArea == null ? coordsSet : shiftCoords(coordsSet, shootingArea);
    }

    public int getFullHeight(WebDriver driver) {
        return ((Number) execute(PAGE_HEIGHT_JS, driver)).intValue();
    }

    public int getFullWidth(WebDriver driver) {
        return ((Number) execute(VIEWPORT_WIDTH_JS, driver)).intValue();
    }

    public int getWindowHeight(WebDriver driver) {
        return ((Number) execute(VIEWPORT_HEIGHT_JS, driver)).intValue();
    }

    protected int getCurrentScrollY(JavascriptExecutor js) {
        return ((Number) js.executeScript("return window.scrollY;")).intValue();
    }

    protected void scrollVertically(JavascriptExecutor js, int scrollY) {
        js.executeScript("scrollTo(0, arguments[0]); return [];", scrollY);
    }

    private Coords getShootingCoords(Set<Coords> coords, int pageWidth, int pageHeight, int viewPortHeight) {
        if (coords == null || coords.isEmpty()) {
            return new Coords(0, 0, pageWidth, pageHeight);
        } else {
            return extendShootingArea(Coords.unity(coords), viewPortHeight, pageHeight);
        }
    }

    private Set<Coords> shiftCoords(Set<Coords> coordsSet, Coords shootingArea) {
        Set<Coords> shiftedCoords = new HashSet<>();
        if (coordsSet != null) {
            for (Coords coords : coordsSet) {
                coords.y -= shootingArea.y;
                shiftedCoords.add(coords);
            }
        }
        return shiftedCoords;
    }

    private Coords extendShootingArea(Coords shootingCoords, int viewportHeight, int pageHeight) {
        int halfViewport = viewportHeight / 2;
        shootingCoords.y = Math.max(shootingCoords.y - halfViewport / 2, 0);
        shootingCoords.height = Math.min(shootingCoords.height + halfViewport, pageHeight);
        return shootingCoords;
    }

    private void waitForScrolling() {
        try {
            Thread.sleep(scrollTimeout);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Exception while waiting for scrolling", e);
        }
    }
}
