package ru.yandex.qatools.ashot.screentaker;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 *         <p/>
 *         Pastes together parts of screenshots
 *         Used when driver shoots viewport only
 */

public abstract class VerticalPastingShootingStrategy extends HeadCuttingShootingStrategy {

    protected int scrollTimeout = 0;

    public void setScrollTimeout(int scrollTimeout) {
        this.scrollTimeout = scrollTimeout;
    }

    protected VerticalPastingShootingStrategy(int scrollTimeout, int headerToCut) {
        super(headerToCut);
        this.scrollTimeout = scrollTimeout;
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
        Coords shootingArea = getShootingCoords(coordsSet, pageWidth, pageHeight, viewportHeight);
        shiftCoords(coordsSet, shootingArea);

        BufferedImage finalImage = new BufferedImage(pageWidth, shootingArea.height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = finalImage.createGraphics();

        int scrollTimes = (int) Math.ceil(shootingArea.getHeight() / viewportHeight);
        for (int n = 0; n < scrollTimes; n++) {
            scrollVertically(js, shootingArea.y + viewportHeight * n);
            waitForScrolling();
            BufferedImage part = super.getScreenshot(wd);
            graphics.drawImage(part, 0, getCurrentScrollY(js, shootingArea.y), null);
        }

        graphics.dispose();
        return finalImage;
    }

    private void waitForScrolling() {
        try {
            Thread.sleep(scrollTimeout);
        } catch (InterruptedException ignored) {
        }
    }

    public abstract int getFullHeight(WebDriver driver);

    public abstract int getFullWidth(WebDriver driver);

    public abstract int getWindowHeight(WebDriver driver);

    private Coords getShootingCoords(Set<Coords> coords, int pageWidth, int pageHeight, int viewPortHeight) {
        if (coords == null || coords.isEmpty()) {
            return new Coords(0, 0, pageWidth, pageHeight);
        } else {
            return extendShootingArea(Coords.unity(coords), viewPortHeight, pageHeight);
        }
    }

    protected int getCurrentScrollY(JavascriptExecutor js, int offsetY) {
        return ((Number) js.executeScript("return window.scrollY;")).intValue() - offsetY;
    }

    protected void scrollVertically(JavascriptExecutor js, int scrollY) {
        js.executeScript("scrollTo(0, arguments[0]); return [];", scrollY);
    }

    private void shiftCoords(Set<Coords> coordsSet, Coords shootingArea) {
        if (coordsSet != null && !coordsSet.isEmpty()) {
            for (Coords coords : coordsSet) {
                coords.y -= shootingArea.y;
            }
        }
    }

    private Coords extendShootingArea(Coords shootingCoords, int viewportHeight, int pageHeight) {
        int halfViewport = viewportHeight / 2;
        shootingCoords.y = Math.max(shootingCoords.y - halfViewport / 2, 0);
        shootingCoords.height = Math.min(shootingCoords.height + halfViewport, pageHeight);
        return shootingCoords;
    }
}
