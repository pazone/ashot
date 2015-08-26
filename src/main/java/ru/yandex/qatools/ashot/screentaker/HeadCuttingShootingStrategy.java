package ru.yandex.qatools.ashot.screentaker;

import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class HeadCuttingShootingStrategy extends ShootingStrategy {

    private HeaderDetectionStrategy headerDetectionStrategy;

    /**
     * Constructor for fixed height header.
     * @param headerToCut - height of header in pixels
     */
    public HeadCuttingShootingStrategy(int headerToCut) {
        this.headerDetectionStrategy = new FixedHeaderDetectionStrategy(headerToCut);
    }

    /**
     * For variable header height use this constructor with {@see VariableHeaderCuttingStrategy}.
     * @param headerDetectionStrategy - strategy to get height of browser's header
     */
    public HeadCuttingShootingStrategy(HeaderDetectionStrategy headerDetectionStrategy) {
        this.headerDetectionStrategy = headerDetectionStrategy;
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd) {
        BufferedImage baseImage = simple().getScreenshot(wd);
        int h = baseImage.getHeight();
        int w = baseImage.getWidth();
        final int headerToCut = getHeaderToCut(wd);
        return baseImage.getSubimage(0, headerToCut, w, h - headerToCut);
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd, Set<Coords> coords) {
        return getScreenshot(wd);
    }

    protected int getHeaderToCut(WebDriver wd) {
        return headerDetectionStrategy.getHeaderHeight(wd);
    }
}
