package ru.yandex.qatools.ashot.screentaker;

import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class HeadCuttingShootingStrategy extends ShootingStrategy {

    protected int headerToCut;

    public HeadCuttingShootingStrategy(int headerToCut) {
        this.headerToCut = headerToCut;
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd) {
        BufferedImage baseImage = simple().getScreenshot(wd);
        int h = baseImage.getHeight();
        int w = baseImage.getWidth();
        return baseImage.getSubimage(0, headerToCut, w, h - headerToCut);
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd, Set<Coords> coords) {
        return getScreenshot(wd);
    }
}
