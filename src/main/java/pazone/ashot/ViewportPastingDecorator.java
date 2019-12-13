package pazone.ashot;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import pazone.ashot.coordinates.Coords;
import pazone.ashot.util.InnerScript;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Will scroll viewport and shoot to get an image of full page.
 * Useful for browsers on portable devices.
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */
public class ViewportPastingDecorator extends ShootingDecorator {

    public static final String PAGE_DIMENSIONS_JS = "js/page_dimensions.js";

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
        PageDimensions pageDimensions = getPageDimensions(wd);
        shootingArea = getShootingCoords(coordsSet, pageDimensions);

        BufferedImage finalImage = new BufferedImage(pageDimensions.getViewportWidth(), shootingArea.height,
                BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = finalImage.createGraphics();

        int viewportHeight = pageDimensions.getViewportHeight();
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

    protected PageDimensions getPageDimensions(WebDriver driver) {
        Map<String, Number> pageDimensions = InnerScript.execute(PAGE_DIMENSIONS_JS, driver);
        return new PageDimensions(pageDimensions.get("pageHeight").intValue(),
                pageDimensions.get("viewportWidth").intValue(), pageDimensions.get("viewportHeight").intValue());
    }

    protected int getCurrentScrollY(JavascriptExecutor js) {
        return ((Number) js.executeScript("var scrY = window.pageYOffset;"
        		+ "if(scrY){return scrY;} else {return 0;}")).intValue();
    }

    protected void scrollVertically(JavascriptExecutor js, int scrollY) {
        js.executeScript("scrollTo(0, arguments[0]); return [];", scrollY);
    }

    private Coords getShootingCoords(Set<Coords> coords, PageDimensions pageDimensions) {
        if (coords == null || coords.isEmpty()) {
            return new Coords(0, 0, pageDimensions.getViewportWidth(), pageDimensions.getPageHeight());
        }
        return extendShootingArea(Coords.unity(coords), pageDimensions);
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

    private Coords extendShootingArea(Coords shootingCoords, PageDimensions pageDimensions) {
        int halfViewport = pageDimensions.getViewportHeight() / 2;
        shootingCoords.y = Math.max(shootingCoords.y - halfViewport / 2, 0);
        shootingCoords.height = Math.min(shootingCoords.height + halfViewport, pageDimensions.getPageHeight());
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
